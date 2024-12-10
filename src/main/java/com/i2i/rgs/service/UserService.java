package com.i2i.rgs.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import com.i2i.rgs.dto.*;
import com.i2i.rgs.util.CsvUtility;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.i2i.rgs.mapper.UserMapper;
import com.i2i.rgs.model.Project;
import com.i2i.rgs.model.User;
import com.i2i.rgs.repository.UserRepository;
import com.i2i.rgs.helper.RGSException;
import com.i2i.rgs.helper.UnAuthorizedException;
import com.i2i.rgs.util.JwtUtil;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * Service class that handles business logic related to user.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final ProjectService projectService;

    /**
     * <p>
     * Saves an user to the database.
     * </p>
     *
     * @param user model to save. Must have its ID assigned manually.
     * @throws RGSException if any issue with the server.
     */
    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error in saving user with id : {}", user.getId(), e);
            throw new RGSException("Cannot save user. Try Again");
        }
    }

    /**
     * <p>
     * Creates a new User object after checking if the user is already present.
     * </p>
     *
     * @param userDTO to create new user.
     * @throws DuplicateKeyException if the user is already present with same email
     */
    public Map<String, Object> addUser(CreateUserDto userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            logger.error("User with same Email Id exists");
            throw new DuplicateKeyException("User with same Email exists");
        }
        User user = UserMapper.createDtoToModel(userDTO);
        user.setHashedPassword(encoder.encode(userDTO.getPassword()));
        user.setAudit("USER");
        saveUser(user);
        logger.info("User added successfully with name: {}", user.getName());
        return Map.of("token", JwtUtil.generateToken(userDTO.getEmail()), "isFinance", user.getIsFinance());
    }

    /**
     * <p>
     * Updates the user with new details.
     * </p>
     *
     * @param userDto to update all the details of the user.
     * @return {@link UserDto} updated details of a user.
     */
    public UserResponseDto updateUser(CreateUserDto userDto) {
        User user = UserMapper.createDtoToModel(userDto);
        user = saveUser(user);
        logger.info("User updated successfully for ID: {}", user.getId());
        return UserMapper.modelToResponseDto(user);
    }

    /**
     * <p>
     * Retrieves an user model of given ID.
     * </p>
     *
     * @param id of the user to retrieve.
     * @return {@link UserResponseDto} model of the user.
     * @throws NoSuchElementException if no user is found with the given ID.
     */
    public UserResponseDto getUserById(String id) {
        User user = getUserModelById(id);
        return UserMapper.modelToResponseDto(user);
    }

    /**
     * <p>
     * Retrieves all users of specified page and size
     * </p>
     *
     * @return {@link UserResponseDto} set containing all users information
     */
    public Set<UserResponseDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<User> userPage = userRepository.findAllByIsDeletedFalse(pageable);
        logger.info("Displayed user details for page : {}", page);
        return userPage.stream()
                .map(UserMapper::modelToResponseDto)
                .collect(Collectors.toSet());
    }

    /**
     * <p>
     * Retrieves an user model of given ID.
     * </p>
     *
     * @param id of the user to retrieve.
     * @return {@link User} model of the user.
     * @throws NoSuchElementException if no user is found with the given ID.
     */
    protected User getUserModelById(String id) {
        User user = userRepository.findByIdAndIsDeletedFalse(id);
        if (user == null) {
            logger.error("User not found for the given id: {}", id);
            throw new NoSuchElementException("User not found for the given id: " + id);
        }
        logger.info("Retrieved user for the given id: {}", id);
        return user;
    }

    /**
     * <p>
     * Deletes an user based on the given ID.
     * </p>
     *
     * @param id of the user for deleting.
     */
    public void deleteUser(String id) {
        User user = getUserModelById(id);
        user.setIsDeleted(true);
        saveUser(user);
        logger.info("User removed successfully with ID: {}", id);
    }

    /**
     * <p>
     * Login an user based on the provided Username and password. Generates new token for a user.
     * </p>
     *
     * @param userDTO containing username and password
     * @return {@link String} created token for the authenticated user.
     * @throws UnAuthorizedException when a user is not authorized.
     */
    public Map<String, Object> authenticateUser(LoginDto userDTO) {
        try {
            authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userDTO.getUsername(), userDTO.getPassword()
                            )
                    );
            String token = JwtUtil.generateToken(userDTO.getUsername());
            return Map.of("token", token, "isFinance", getUserModelByEmail(userDTO.getUsername()).getIsFinance());
        } catch (BadCredentialsException e) {
            logger.error("Invalid username or password", e);
            throw new UnAuthorizedException("Invalid Username or Password");
        }
    }

    private User getUserModelByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.error("User not found for the given email: {}", email);
            throw new NoSuchElementException("User not found for the given email: " + email);
        }
        logger.info("Retrieved user for the given email: {}", email);
        return user;
    }

    public void assignProjects(String email, Set<CreateProjectDto> projectDtos) {
        User user = getUserModelByEmail(email);
        Set<Project> projects = new HashSet<>();
        for (CreateProjectDto proj : projectDtos) {
            Project project = projectService.getModel(proj.getId());
            projects.add(project);
        }
        user.setProjects(projects);
        saveUser(user);
    }

    public void saveUsersFromCsv(MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        List<User> users = CsvUtility.csvToUserList(is);
        userRepository.saveAll(users);
    }

    public byte[] exportUsersToCsv() throws IOException {
        List<User> users = userRepository.findAll(); // Fetch all users from the database
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        CsvUtility.writeUsersToCsv(users, outputStream);
        return outputStream.toByteArray();
    }
}
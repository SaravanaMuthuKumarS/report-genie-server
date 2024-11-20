package com.i2i.rgs.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

import com.bicart.dto.ResponseUserDto;
import com.bicart.dto.UserDto;
import com.bicart.dto.UserRoleDto;
import com.bicart.helper.CustomException;
import com.bicart.helper.UnAuthorizedException;
import com.bicart.mapper.UserMapper;
import com.bicart.model.Role;
import com.bicart.model.User;
import com.bicart.repository.UserRepository;
import com.bicart.util.JwtUtil;

/**
 * <p>
 *     Service class that handles business logic related to user.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    /**
     * <p>
     * Saves an user to the database.
     * </p>
     *
     * @param user model to save. Must have its ID assigned manually.
     * @throws CustomException if any issue with the server.
     */
    public void saveUser(User user) {
        try {
            userRepository.save(user);
            logger.info("User saved successfully with id : {}", user.getId());
        } catch (Exception e) {
            logger.error("Error in saving user with id : {}", user.getId(), e);
            throw new CustomException("Cannot save user. Try Again");
        }
    }

    /**
     * <p>
     * Creates a new User object after checking if the user is already present.
     * </p>
     *
     * @param userDTO to create new user.
     * @throws DuplicateKeyException if the user is already present with same mobile or email
     */
    public void addUser(UserDto userDTO) {
        User user = UserMapper.dtoToModel(userDTO);
        if (userRepository.existsByEmailOrMobileNumber(userDTO.getEmail(), userDTO.getMobileNumber())) {
            logger.error("User with same Email Id or Mobile Number exists");
            throw new DuplicateKeyException("User with same Email or Mobile Number exists");
        }
        user.setId(UUID.randomUUID().toString());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setAudit(user.getId());
        user.setRole(Set.of(roleService.getRoleModelByName("USER")));
        saveUser(user);
        logger.info("User added successfully with name: {}", user.getName());
    }

    /**
     * <p>
     * Updates the user with new details.
     * </p>
     *
     * @param userDto to update the all the details of the user.
     * @return {@link UserDto} updated details of a user.
     */
    public UserDto updateUser(UserDto userDto) {
        User user = UserMapper.dtoToModel(userDto);
        saveUser(user);
        logger.info("User updated successfully for ID: {}", userDto.getId());
        return UserMapper.modelToDto(user);
    }

    /**
     * <p>
     * Retrieves all users of specified page and size
     * </p>
     *
     * @return {@link ResponseUserDto} set containing all users information
     */
    public Set<ResponseUserDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<User> userPage = userRepository.findAllByIsDeletedFalse(pageable);
        logger.info("Displayed user details for page : {}", page);
        return userPage.stream()
                .map(UserMapper::modelToResponseUserDto)
                .collect(Collectors.toSet());
    }

    /**
     * <p>
     * Retrieves a user with given id
     * </p>
     *
     * @param id the ID of the user whose details are to be viewed
     */
    public UserRoleDto getUser(String id) {
        User user = getUserModelById(id);
        logger.info("Retrieved user details for ID: {}", id);
        return UserMapper.modelToUserRoleDto(user);
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
        Set<Role> roles = user.getRole();
        if (roles != null && !roles.isEmpty()) {
            user.setRole(null);
        }
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
    public String authenticateUser(UserDto userDTO) {
        try {
            authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userDTO.getEmail(), userDTO.getPassword()
                            )
                    );
            return JwtUtil.generateToken(userDTO.getEmail());
        } catch (BadCredentialsException e) {
            logger.error("Invalid username or password", e);
            throw new UnAuthorizedException("Invalid Username or Password");
        }
    }

    /**
     * <p>
     * Makes an user an admin.
     * </p>
     *
     * @param userDto to make an admin. Must contain the ID of the user.
     * @throws CustomException if an error occurs while making a user an admin.
     */
    public void makeAdmin(UserDto userDto) {
        User user = getUserModelById(userDto.getId());
        try {
            Set<Role> roles = user.getRole();
            roles.add(roleService.getRoleModelByName("ADMIN"));
            user.setRole(roles);
        } catch (Exception e) {
            logger.error("Error in setting role for user", e);
            throw new CustomException("Cannot apply role to an user");
        }
        saveUser(user);
        logger.info("User updated successfully for ID: {}", userDto.getId());
    }
}


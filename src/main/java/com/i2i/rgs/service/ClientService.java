package com.i2i.rgs.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.i2i.rgs.dto.ClientDto;
import com.i2i.rgs.dto.CreateClientDto;
import com.i2i.rgs.mapper.ClientMapper;
import com.i2i.rgs.model.Client;
import com.i2i.rgs.model.Project;
import com.i2i.rgs.repository.ClientRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    private Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public void addClient(CreateClientDto clientToAdd) {
        Client client = ClientMapper.dtoToModel(clientToAdd);
        saveClient(client);
    }

    public ClientDto getByName(String name) {
        return ClientMapper.modelToDtoWithProjects(clientRepository.findByName(name));
    }

    public Set<String> getAllClients() {
        List<Client> clients = clientRepository.findAllByIsDeletedFalse();
        return clients.stream()
                .map(Client::getName)
                .collect(Collectors.toSet());
    }

    public Client getModel(String clientName) {
        return clientRepository.findByName(clientName);
    }
}

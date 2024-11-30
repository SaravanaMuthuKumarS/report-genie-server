package com.i2i.rgs.controller;

import java.util.Map;
import java.util.Set;

import com.i2i.rgs.dto.ClientDto;
import com.i2i.rgs.dto.CreateClientDto;
import com.i2i.rgs.helper.SuccessResponse;
import com.i2i.rgs.service.ClientService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> addClient(@RequestBody CreateClientDto client) {
        clientService.addClient(client);
        return SuccessResponse.setSuccessResponseCreated("Client added successfully", null);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse> getClients() {
        Set<String> clients = clientService.getAllClients();
        return SuccessResponse.setSuccessResponseOk("Client fetched successfully", Map.of("clients", clients));
    }

    @GetMapping("/{clientName}")
    public ResponseEntity<SuccessResponse> getClient(@PathVariable String clientName) {
        ClientDto client = clientService.getByName(clientName);
        return SuccessResponse.setSuccessResponseOk("Client fetched successfully", Map.of("client", client));
    }
}

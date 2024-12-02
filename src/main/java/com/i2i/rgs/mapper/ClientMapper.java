package com.i2i.rgs.mapper;

import com.i2i.rgs.dto.ClientDto;
import com.i2i.rgs.dto.CreateClientDto;
import com.i2i.rgs.model.Client;

public class ClientMapper {
    public static Client dtoToModel(CreateClientDto clientToAdd) {
        return Client.builder()
                .name(clientToAdd.getName())
                .build();
    }

    public static CreateClientDto modelToDto(Client client) {
        return CreateClientDto.builder()
                .name(client.getName())
                .build();
    }

    public static ClientDto modelToResponseDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .build();
    }
}

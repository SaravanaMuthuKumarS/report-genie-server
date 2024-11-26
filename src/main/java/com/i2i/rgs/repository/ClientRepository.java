package com.i2i.rgs.repository;

import java.util.List;

import com.i2i.rgs.model.Client;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {
    Client findByName(String name);

    List<Client> findAllByIsDeletedFalse();
}

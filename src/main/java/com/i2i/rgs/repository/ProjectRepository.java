package com.i2i.rgs.repository;

import java.util.List;

import com.i2i.rgs.model.Project;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {
    Project findByName(String name);

    List<Project> findAllByIsDeletedFalse();
}

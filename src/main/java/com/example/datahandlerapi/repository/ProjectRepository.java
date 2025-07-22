package com.example.datahandlerapi.repository;

import com.example.datahandlerapi.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByProjectName(String projectName);
}

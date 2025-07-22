package com.example.datahandlerapi.service;

import com.example.datahandlerapi.dto.ProjectDTO;
import com.example.datahandlerapi.entity.Project;

import java.util.List;

public interface ProjectService {
    ProjectDTO createProject(ProjectDTO dto);
    List<Project> getAllProjects();
    Project getProjectById(Long id);
    Project updateProject(Long id, Project project);
    void deleteProject(Long id);
}

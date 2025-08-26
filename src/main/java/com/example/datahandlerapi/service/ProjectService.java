package com.example.datahandlerapi.service;

import com.example.datahandlerapi.dto.ProjectDTO;
import com.example.datahandlerapi.entity.Project;

import java.util.List;
import java.util.Map;

public interface ProjectService {
    ProjectDTO createProject(ProjectDTO dto);
    List<ProjectDTO> getAllProjects();
    Project getProjectById(Long id);
//    ProjectDTO updateProject(Long id, ProjectDTO project);
    void deleteProject(Long id);
    List<Map<String, Object>> getProjectDropdownData();
}

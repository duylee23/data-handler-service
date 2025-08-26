package com.example.datahandlerapi.service.impl;

import com.example.datahandlerapi.dto.ProjectDTO;
import com.example.datahandlerapi.entity.Project;
import com.example.datahandlerapi.mapper.ProjectMapper;
import com.example.datahandlerapi.repository.ProjectRepository;
import com.example.datahandlerapi.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectServiceImplement implements ProjectService {
    ProjectRepository projectRepository;
    ProjectMapper projectMapper;

    @Override
    public ProjectDTO createProject(ProjectDTO dto) {
        if(this.projectRepository.existsByProjectName(dto.getName())) {
            throw new IllegalArgumentException("Project name already exist!");
        }
        Project project = this.projectMapper.toEntity(dto);
        project.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return this.projectMapper.toDTO(projectRepository.save(project));
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this.projectMapper::toDTO) // hoặc viết tay hàm toDTO như trên
                .collect(Collectors.toList());
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id: " + id));
    }

//    @Override
//    public ProjectDTO updateProject(Long id, ProjectDTO updatedProject) {
//        Project existing = getProjectById(id);
//        existing.setProjectName(updatedProject.getProjectName());
//        existing.setDescription(updatedProject.getDescription());
//        existing.setCreatedBy(updatedProject.getCreatedBy());
//        Project result = projectRepository.save(existing);
//        return
//    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> getProjectDropdownData() {
        List<Project> projects = this.projectRepository.findAll();
        return projects.stream().map(project -> {
            Map<String, Object> mapItem = new HashMap<>();
            mapItem.put("id", project.getId());
            mapItem.put("name", project.getProjectName());
            return mapItem;
        }).toList();
    }
}

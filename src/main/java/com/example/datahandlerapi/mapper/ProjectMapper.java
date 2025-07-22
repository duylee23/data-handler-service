package com.example.datahandlerapi.mapper;

import com.example.datahandlerapi.dto.ProjectDTO;
import com.example.datahandlerapi.entity.Group;
import com.example.datahandlerapi.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    public ProjectDTO toDTO(Project project) {
        if (project == null) return null;
        return ProjectDTO.builder()
                .id(project.getId())
                .name(project.getProjectName())
                .description(project.getDescription())
                .createdBy(project.getCreatedBy())
                .createdAt(project.getCreatedAt())
                .groupListName(project.getGroups() == null ? null :
                        project.getGroups().stream()
                                .map(Group::getGroupName)
                                .toList())
                .build();
    }

    public Project toEntity(ProjectDTO dto) {
        if (dto == null) return null;
        Project project = new Project();
        project.setId(dto.getId());
        project.setProjectName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setCreatedBy(dto.getCreatedBy());
        return project;
    }
}

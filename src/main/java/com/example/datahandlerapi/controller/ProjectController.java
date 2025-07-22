package com.example.datahandlerapi.controller;


import com.example.datahandlerapi.dto.ProjectDTO;
import com.example.datahandlerapi.dto.response.ApiResponse;
import com.example.datahandlerapi.entity.Project;
import com.example.datahandlerapi.mapper.ProjectMapper;
import com.example.datahandlerapi.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    ProjectService projectService;
    ProjectMapper projectMapper;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<ProjectDTO>> createProject(@RequestBody ProjectDTO dto) {
        ProjectDTO createdProject = this.projectService.createProject(dto);
        return ResponseEntity.ok(
                ApiResponse.<ProjectDTO>builder()
                        .status("success")
                        .message("Create new project successfully!")
                        .data(createdProject)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        return ResponseEntity.ok(projectService.updateProject(id, project));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();

    }
}

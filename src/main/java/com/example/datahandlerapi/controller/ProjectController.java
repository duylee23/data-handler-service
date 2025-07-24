package com.example.datahandlerapi.controller;


import com.example.datahandlerapi.dto.ProjectDTO;
import com.example.datahandlerapi.dto.response.ApiResponse;
import com.example.datahandlerapi.entity.Project;
import com.example.datahandlerapi.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/project")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    ProjectService projectService;

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

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Project>>> getAllProjects() {
        List<Project> projectList = this.projectService.getAllProjects();
        return ResponseEntity.ok(
                ApiResponse.<List<Project>>builder()
                        .status("success")
                        .message("Get all projects")
                        .data(projectList)
                        .build()
        );
    }

    @GetMapping("/dropdown")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAllProjectDropdown() {
        List<Map<String, Object>> result = this.projectService.getProjectDropdownData();
        return ResponseEntity.ok(
                ApiResponse.<List<Map<String, Object>>>builder()
                        .status("success")
                        .message("Get project dropdown successfully")
                        .data(result)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status("success")
                        .message("Delete project successfully!")
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        return ResponseEntity.ok(projectService.updateProject(id, project));
    }
}

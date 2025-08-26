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
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProjectController {

    ProjectService projectService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<ProjectDTO>> createProject(@RequestBody ProjectDTO dto) {
        ProjectDTO created = projectService.createProject(dto);
        return ResponseEntity.ok(ApiResponse.success("Project created successfully", created));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> getAllProjects() {
        List<ProjectDTO> items = projectService.getAllProjects();
        return ResponseEntity.ok(ApiResponse.success("Project list retrieved successfully", items));
    }

    @GetMapping("/dropdown")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAllProjectDropdown() {
        List<Map<String, Object>> result = projectService.getProjectDropdownData();
        return ResponseEntity.ok(ApiResponse.success("Project dropdown data retrieved successfully", result));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.success("Project deleted successfully"));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResponse<ProjectDTO>> updateProject(
//            @PathVariable Long id,
//            @RequestBody ProjectDTO dto
//    ) {
//        ProjectDTO updated = projectService.updateProject(id, dto);
//        return ResponseEntity.ok(ApiResponse.success("Project updated successfully", updated));
//    }
}

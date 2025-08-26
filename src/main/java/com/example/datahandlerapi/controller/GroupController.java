package com.example.datahandlerapi.controller;

import com.example.datahandlerapi.dto.GroupDTO;
import com.example.datahandlerapi.dto.response.ApiResponse;
import com.example.datahandlerapi.service.GroupService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GroupController {

    GroupService groupService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<GroupDTO>> createGroup(@RequestBody GroupDTO dto) {
        GroupDTO createdGroup = groupService.createGroup(dto);
        return ResponseEntity.ok(ApiResponse.success("Group created successfully", createdGroup));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<GroupDTO>>> getGroupList() {
        List<GroupDTO> groupList = groupService.getListGroup();
        return ResponseEntity.ok(ApiResponse.success("Group list retrieved successfully", groupList));
    }

    @GetMapping("/types")
    public ResponseEntity<ApiResponse<List<String>>> getGroupTypes() {
        List<String> groupTypes = groupService.getGroupTypes();
        return ResponseEntity.ok(ApiResponse.success("Group type retrieved successfully", groupTypes));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.ok(ApiResponse.success("Group deleted successfully"));
    }
}

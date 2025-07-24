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
        GroupDTO createdGroup = this.groupService.createGroup(dto);
        return ResponseEntity.ok(
          ApiResponse.<GroupDTO>builder()
                  .status("success")
                  .message("Created group successfully! ")
                  .data(createdGroup)
                  .build()
        );
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<GroupDTO>>> getGroupList() {
        List<GroupDTO> groupList = this.groupService.getListGroup();
        return ResponseEntity.ok(
                ApiResponse.<List<GroupDTO>>builder()
                        .status("success")
                        .message("Group list! ")
                        .data(groupList)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(@PathVariable Long id) {
        this.groupService.deleteGroup(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status("success")
                        .message("Delete group successfully")
                        .build()
        );
    }

}

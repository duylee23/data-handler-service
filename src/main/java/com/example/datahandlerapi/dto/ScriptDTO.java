package com.example.datahandlerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScriptDTO {
    Long id;
    String name;
    String description;
    String scriptPath;
    String createdBy;
    String updatedBy;
    LocalDateTime createdTime;
    LocalDateTime updatedTime;
    Long groupId; // foreign key
}

package com.example.datahandlerapi.dto;

import com.example.datahandlerapi.entity.Group;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class ProjectDTO {
    Long id;
    String name;
    String description;
    String createdBy;
    Timestamp createdAt;
    List<String> groupListName;
}

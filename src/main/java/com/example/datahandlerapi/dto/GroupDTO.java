package com.example.datahandlerapi.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupDTO {
    Long id;
    String name;
    String description;
    String createdBy;
    Timestamp createdAt;
    List<String> scriptListName;
    List<Map<String,Object>> scripts;
    Long projectId;
    String projectName;
}

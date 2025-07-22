package com.example.datahandlerapi.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScriptRequest {
    private String name;
    private String description;
    private String groupName;
}

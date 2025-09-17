package com.example.datahandlerapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonFilterRequest {
    private Long page;
    private Long size;
    private String searchName;
    private String userName;
    private String groupName;
}

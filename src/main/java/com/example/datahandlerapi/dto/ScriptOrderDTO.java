package com.example.datahandlerapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScriptOrderDTO {
    private Long scriptId;
    private Integer order;
    private String scriptName;
}

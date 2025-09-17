package com.example.datahandlerapi.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class RunScriptHistoryDTO {
    private Long id;

    private String username;

    private String groupName;

    private String status;

    private String runTime;

}

package com.example.datahandlerapi.mapper;
import com.example.datahandlerapi.dto.GroupDTO;
import com.example.datahandlerapi.dto.ScriptOrderDTO;
import com.example.datahandlerapi.entity.Group;
import com.example.datahandlerapi.entity.Script;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {
    public GroupDTO toDTO(Group group) {
        if (group == null) return null;

        return GroupDTO.builder()
                .id(group.getId())
                .name(group.getGroupName())
                .description(group.getDescription())
                .createdBy(group.getCreatedBy())
                .createdAt(group.getCreatedAt())
                .projectName(group.getProject() != null ? group.getProject().getProjectName() : null)
                .projectId(group.getProject() != null ? group.getProject().getId() : null)
                .scriptListName(group.getScripts().stream().map(Script::getName).toList())
                .scripts(group.getScripts().stream().map(s -> ScriptOrderDTO.builder()
                        .scriptId(s.getId())
                        .order(s.getExecOrder())
                        .scriptName(s.getName())
                        .build()).toList())
                .build();
    }

    public Group toEntity(GroupDTO dto){
        if(dto == null) return null;
        Group group = new Group();
        group.setGroupName(dto.getName());
        group.setDescription(dto.getDescription());
        group.setCreatedBy(dto.getCreatedBy());
        return group;
    }
}

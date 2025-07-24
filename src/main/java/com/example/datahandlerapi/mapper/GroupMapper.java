package com.example.datahandlerapi.mapper;

import com.example.datahandlerapi.dto.GroupDTO;
import com.example.datahandlerapi.entity.Group;
import com.example.datahandlerapi.entity.Script;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {
    public GroupDTO toDTO(Group group){
        if(group == null) return null;
        return GroupDTO.builder()
                .groupName(group.getGroupName())
                .groupDescription(group.getDescription())
                .createdBy(group.getCreatedBy())
                .createdAt(group.getCreatedAt())
                .scriptListName(group.getScripts().stream().map(Script::getName).toList())
                .build();
    }

    public Group toEntity(GroupDTO dto){
        if(dto == null) return null;
        Group group = new Group();
        group.setGroupName(dto.getGroupName());
        group.setDescription(dto.getGroupDescription());
        group.setCreatedBy(dto.getCreatedBy());
        return group;
    }
}

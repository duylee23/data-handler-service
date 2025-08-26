package com.example.datahandlerapi.mapper;
import com.example.datahandlerapi.dto.GroupDTO;
import com.example.datahandlerapi.entity.Group;
import com.example.datahandlerapi.entity.Script;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GroupMapper {
    public GroupDTO toDTO(Group group) {
        if (group == null) return null;

        List<Map<String, Object>> scriptInfo = group.getScripts().stream()
                .map(script -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("script", script.getName());
                    map.put("order", script.getExecOrder());
                    return map;
                }).toList();

        return GroupDTO.builder()
                .id(group.getId())
                .name(group.getGroupName())
                .description(group.getDescription())
                .createdBy(group.getCreatedBy())
                .createdAt(group.getCreatedAt())
                .projectName(group.getProject() != null ? group.getProject().getProjectName() : null)  // <- THÊM DÒNG NÀY
                .projectId(group.getProject() != null ? group.getProject().getId() : null)         // <- THÊM DÒNG NÀY
                .scriptListName(group.getScripts().stream().map(Script::getName).toList())
                .scripts(scriptInfo)
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

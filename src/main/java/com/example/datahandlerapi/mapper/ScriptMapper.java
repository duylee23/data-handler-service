package com.example.datahandlerapi.mapper;

import com.example.datahandlerapi.dto.ScriptDTO;
import com.example.datahandlerapi.entity.Group;
import com.example.datahandlerapi.entity.Script;
import com.example.datahandlerapi.repository.GroupRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScriptMapper {
    GroupRepository groupRepository;

    public ScriptDTO toDTO(Script script) {
        return ScriptDTO.builder()
                .id(script.getId())
                .name(script.getName())
                .description(script.getDescription())
                .scriptPath(script.getScriptPath())
                .createdBy(script.getCreatedBy())
                .updatedBy(script.getUpdatedBy())
                .createdTime(script.getCreatedTime())
                .updatedTime(script.getUpdatedTime())
                .groupId(script.getGroup() != null ? script.getGroup().getId() : null)
                .build();
    }

    public Script toEntity(ScriptDTO dto) {
        Script script = new Script();
        script.setId(dto.getId());
        script.setName(dto.getName());
        script.setDescription(dto.getDescription());
        script.setScriptPath(dto.getScriptPath());
        script.setCreatedBy(dto.getCreatedBy());
        script.setUpdatedBy(dto.getUpdatedBy());
        script.setCreatedTime(dto.getCreatedTime());
        script.setUpdatedTime(dto.getUpdatedTime());

        if (dto.getGroupId() != null) {
            Group group = groupRepository.findById(dto.getGroupId())
                    .orElseThrow(() -> new IllegalArgumentException("Group not found with ID: " + dto.getGroupId()));
            script.setGroup(group);
        }
        return script;
    }
}

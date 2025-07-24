package com.example.datahandlerapi.service.impl;

import com.example.datahandlerapi.dto.GroupDTO;
import com.example.datahandlerapi.entity.Group;
import com.example.datahandlerapi.entity.Script;
import com.example.datahandlerapi.mapper.GroupMapper;
import com.example.datahandlerapi.repository.GroupRepository;
import com.example.datahandlerapi.repository.ScriptRepository;
import com.example.datahandlerapi.service.GroupService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GroupServiceImplement implements GroupService {

    GroupRepository groupRepository;
    GroupMapper groupMapper;
    ScriptRepository scriptRepository;
    @Override
    @Transactional
    public GroupDTO createGroup(GroupDTO dto) {
        if(this.groupRepository.existsByGroupName((dto.getGroupName()))) {
            throw new IllegalArgumentException("Group name already exist!");
        }
        Group group = this.groupMapper.toEntity(dto);
        return this.groupMapper.toDTO(this.groupRepository.save(group));
    }

    @Override
    @Transactional
    public GroupDTO editGroup(GroupDTO dto) {
        Group group = this.groupRepository.findGroupById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Group not exist!"));
        if(dto.getGroupDescription() != null) {
            group.setDescription(dto.getGroupDescription());
        }
        if(dto.getGroupName() != null) {
            group.setGroupName(dto.getGroupName());
        }
        if(dto.getScriptListName() != null) {
            List<Script> addScriptList = new ArrayList<>();
            for(String scriptName : dto.getScriptListName()){
                Script script = this.scriptRepository.findScriptByName(scriptName)
                        .orElseThrow(() -> new IllegalArgumentException("Script not found with " + scriptName));
                addScriptList.add(script);
            }
            group.setScripts(addScriptList);
        }
        return null;
    }

    @Override
    public List<GroupDTO> getListGroup() {
        List<Group> groupList = this.groupRepository.findAll();
        return groupList.stream().map(this.groupMapper::toDTO).toList();
    }

    @Override
    public void deleteGroup(Long id) {
        Group group = this.groupRepository.findGroupById(id)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + id
                ));
        this.groupRepository.delete(group);
    }
}

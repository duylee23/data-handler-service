package com.example.datahandlerapi.service.impl;

import com.example.datahandlerapi.dto.GroupDTO;
import com.example.datahandlerapi.entity.Group;
import com.example.datahandlerapi.entity.Project;
import com.example.datahandlerapi.entity.Script;
import com.example.datahandlerapi.mapper.GroupMapper;
import com.example.datahandlerapi.repository.GroupRepository;
import com.example.datahandlerapi.repository.ProjectRepository;
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
import java.util.Map;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GroupServiceImplement implements GroupService {

    GroupRepository groupRepository;
    GroupMapper groupMapper;
    ScriptRepository scriptRepository;
    ProjectRepository projectRepository;

    @Override
    @Transactional
    public GroupDTO createGroup(GroupDTO dto) {
        if(this.groupRepository.existsByGroupName((dto.getName()))) {
            throw new IllegalArgumentException("Group name already exist!");
        }
        Project project = this.projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project with id: " + dto.getProjectId() + " not found! " ));
        Group group = this.groupMapper.toEntity(dto);
        group.setProject(project);

        Group savedGroup = this.groupRepository.save(group);

        List<Map<String,Object>> scriptListRequest = dto.getScripts();
        List<Script> scriptListEntity = new ArrayList<>();

        for(Map<String, Object> scriptMap : scriptListRequest) {
            Object idObj = scriptMap.get("script");
            if (idObj == null) {
                throw new IllegalArgumentException("Script id is missing in request.");
            }

            Long scriptId = Long.valueOf(idObj.toString());
            Script foundedScript = this.scriptRepository.findScriptsById(scriptId)
                    .orElseThrow(() -> new IllegalArgumentException("Script with id: " + scriptId + " not found! " ));
            foundedScript.setGroup(group);

            if (scriptMap.containsKey("order")) {
                Integer order = Integer.parseInt(scriptMap.get("order").toString());
                foundedScript.setExecOrder(order);
            }

            scriptListEntity.add(foundedScript);
        }
        this.scriptRepository.saveAll(scriptListEntity);

        savedGroup.setScripts(scriptListEntity);

        return this.groupMapper.toDTO(savedGroup);
    }



    @Override
    @Transactional
    public GroupDTO editGroup(GroupDTO dto) {
        Group group = this.groupRepository.findGroupById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Group not exist!"));
        if(dto.getDescription() != null) {
            group.setDescription(dto.getDescription());
        }
        if(dto.getName() != null) {
            group.setGroupName(dto.getName());
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
    @Transactional
    public void deleteGroup(Long groupId) {
        Group group = this.groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group with id " + groupId + " not found!"));

        // Xóa liên kết với script (nếu không cascade REMOVE)
        for (Script script : group.getScripts()) {
            script.setGroup(null); // remove foreign key to avoid constraint error
        }

        this.groupRepository.delete(group);
    }

    @Override
    public List<String> getGroupTypes() {
        List<Group> groupList = this.groupRepository.findAll();
        List<String> groupTypes = groupList.stream().map(Group::getGroupName).toList();
        return groupTypes;
    }


}

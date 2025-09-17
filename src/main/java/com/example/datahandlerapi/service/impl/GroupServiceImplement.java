package com.example.datahandlerapi.service.impl;

import com.example.datahandlerapi.dto.GroupDTO;
import com.example.datahandlerapi.dto.ScriptOrderDTO;
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
        //check if group name is duplicated
        if(this.groupRepository.existsByGroupName((dto.getName()))) {
            throw new IllegalArgumentException("Group name already exist!");
        }

        //check if project id is existed
        Project project = this.projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project with id: " + dto.getProjectId() + " not found! " ));

        Group group = this.groupMapper.toEntity(dto);
        group.setProject(project);

        Group savedGroup = this.groupRepository.save(group);

        List<ScriptOrderDTO> scriptListRequest = dto.getScripts();
        List<Script> scriptListEntity = new ArrayList<>();

        for(ScriptOrderDTO s : scriptListRequest) {
            Long scriptId = s.getScriptId();
            if (scriptId == null) {
                throw new IllegalArgumentException("Script id is missing in request.");
            }

            //check if script is exist by script id
            Script foundedScript = this.scriptRepository.findScriptsById(scriptId)
                    .orElseThrow(() -> new IllegalArgumentException("Script with id: " + scriptId + " not found! " ));

            foundedScript.setGroup(group);

            if (s.getOrder() != null) {
                Integer order = s.getOrder();
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

    @Override
    @Transactional
    public GroupDTO editGroup(GroupDTO dto, Long id) {
        Group group = this.groupRepository.findGroupById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Group with id: " + dto.getId() + " not found !"));

        Project project = this.projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project with id: " + dto.getProjectId() + " not found! " ));

        group.setProject(project);

        // Xoá liên kết script cũ nếu cần
        List<Script> oldScripts = scriptRepository.findByGroupGroupName(group.getGroupName());
        for (Script s : oldScripts) {
            s.setGroup(null);
            s.setExecOrder(null);
        }
        scriptRepository.saveAll(oldScripts);

        List<Script> newScripts = dto.getScripts().stream().map(scriptDto -> {
            Script script = scriptRepository.findScriptsById(scriptDto.getScriptId())
                    .orElseThrow(() -> new IllegalArgumentException("Script with id: " + scriptDto.getScriptId() + " not found!"));
            script.setGroup(group);
            script.setExecOrder(scriptDto.getOrder());
            return script;
        }).toList();

        // Lưu toàn bộ script đã cập nhật
        scriptRepository.saveAll(newScripts);

        // Gán vào group để trả DTO (không bắt buộc nếu chỉ dùng mapper)
        group.setScripts(newScripts);
        return groupMapper.toDTO(group);

    }

}

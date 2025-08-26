package com.example.datahandlerapi.service;

import com.example.datahandlerapi.dto.GroupDTO;

import java.util.List;

public interface GroupService {
    GroupDTO createGroup(GroupDTO dto);
    GroupDTO editGroup(GroupDTO dto);
    List<GroupDTO> getListGroup();
    void deleteGroup(Long id);
    List<String> getGroupTypes();
}

package com.example.datahandlerapi.service;

import com.example.datahandlerapi.dto.RunScriptHistoryDTO;
import com.example.datahandlerapi.dto.ScriptDTO;
import com.example.datahandlerapi.dto.request.CommonFilterRequest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ScriptService {
    ScriptDTO uploadScript(MultipartFile file, ScriptDTO dto);
    List<ScriptDTO> getScriptList();
    void deleteScript(Long id);
    List<Map<String, Object>> getScriptDropdownData();
    Resource downloadScriptsByGroupName(String groupName) throws IOException;
    List<Resource> getScriptsResourceByGroupNames (String groupName);
    List<ScriptDTO> getScriptsByGroupNames(String groupName);
    RunScriptHistoryDTO updateRunScriptHistory(RunScriptHistoryDTO dto);
    Page<RunScriptHistoryDTO> showScriptHistoryList(CommonFilterRequest request);
}

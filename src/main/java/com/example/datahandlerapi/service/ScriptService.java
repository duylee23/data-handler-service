package com.example.datahandlerapi.service;

import com.example.datahandlerapi.dto.ScriptDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ScriptService {
    ScriptDTO uploadScript(MultipartFile file, ScriptDTO dto);
    List<ScriptDTO> getScriptList();
    void deleteScript(Long id);
    List<Map<String, Object>> getScriptDropdownData();
}

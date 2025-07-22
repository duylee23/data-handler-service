package com.example.datahandlerapi.service;

import com.example.datahandlerapi.dto.ScriptDTO;
import com.example.datahandlerapi.dto.request.ScriptRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ScriptService {
    ScriptDTO uploadScript(MultipartFile file, ScriptRequest requestDTO, String createdBy);

}

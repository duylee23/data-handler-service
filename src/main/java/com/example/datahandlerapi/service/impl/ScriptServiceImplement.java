package com.example.datahandlerapi.service.impl;

import com.example.datahandlerapi.dto.ScriptDTO;
import com.example.datahandlerapi.entity.Script;
import com.example.datahandlerapi.mapper.ScriptMapper;
import com.example.datahandlerapi.repository.ScriptRepository;
import com.example.datahandlerapi.service.ScriptService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ScriptServiceImplement implements ScriptService {

    static String UPLOAD_DIR = "/data/scripts";
    ScriptRepository scriptRepository;
    ScriptMapper scriptMapper;


    @Override
    @Transactional
    public ScriptDTO uploadScript(MultipartFile file, ScriptDTO dto) {
        if(file.isEmpty()) throw new IllegalArgumentException("File is empty");
        try{
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if(!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path destinationPath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            Script script = this.scriptMapper.toEntity(dto);
            script.setCreatedTime(LocalDateTime.now());
            script.setScriptPath(destinationPath.toString());
            scriptRepository.save(script);
            return this.scriptMapper.toDTO(script);
        } catch (IOException e){
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }
    }

    @Override
    public List<ScriptDTO> getScriptList() {
        List<Script> scripts = this.scriptRepository.findAll();
        return scripts.stream().map(this.scriptMapper::toDTO).toList();
    }

    @Override
    public void deleteScript(Long id) {
        try{
            Script script = this.scriptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Script not found with ID " + id));
            String scriptPath = script.getScriptPath();
            if(scriptPath != null && !scriptPath.isEmpty()) {
                Path filePath = Paths.get(scriptPath);
                if(Files.exists(filePath)){
                    Files.delete(filePath);
                }
            }else {
                log.warn("File not found: {}", scriptPath);
            }
            this.scriptRepository.delete(script);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete script file: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getScriptDropdownData() {
        List<Script> scripts = this.scriptRepository.findAll();

        return scripts.stream()
                .filter(script -> script.getGroup() == null)
                .map(script -> {
                    Map<String, Object> mapItem = new HashMap<>();
                    mapItem.put("id", script.getId());
                    mapItem.put("name", script.getName());
                    return mapItem;
                })
                .toList();
    }
}

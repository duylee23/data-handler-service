package com.example.datahandlerapi.service.impl;

import com.example.datahandlerapi.dto.ScriptDTO;
import com.example.datahandlerapi.dto.request.ScriptRequest;
import com.example.datahandlerapi.entity.Group;
import com.example.datahandlerapi.entity.Script;
import com.example.datahandlerapi.mapper.ScriptMapper;
import com.example.datahandlerapi.repository.GroupRepository;
import com.example.datahandlerapi.repository.ScriptRepository;
import com.example.datahandlerapi.service.ScriptService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScriptServiceImplement implements ScriptService {

    static String UPLOAD_DIR = "/data/scripts";
    ScriptRepository scriptRepository;
    GroupRepository groupRepository;
    ScriptMapper scriptMapper;


    @Override
    public ScriptDTO uploadScript(MultipartFile file, ScriptRequest requestDTO, String createdBy) {
        if(file.isEmpty()) throw new IllegalArgumentException("File is empty");
        try{
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if(!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path destinationPath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            Group group = this.groupRepository.findGroupByGroupName(requestDTO.getGroupName())
                    .orElseThrow(() -> new RuntimeException("Group not found!"));

            Script script = Script.builder()
                    .name(requestDTO.getName())
                    .description(requestDTO.getDescription())
                    .scriptPath(destinationPath.toString())
                    .createdBy(createdBy)
                    .group(group)
                    .build();

            scriptRepository.save(script);
            return this.scriptMapper.toDTO(script);
        } catch (IOException e){
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }
    }
}

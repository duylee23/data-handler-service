package com.example.datahandlerapi.service.impl;

import com.example.datahandlerapi.dto.RunScriptHistoryDTO;
import com.example.datahandlerapi.dto.ScriptDTO;
import com.example.datahandlerapi.dto.request.CommonFilterRequest;
import com.example.datahandlerapi.entity.Group;
import com.example.datahandlerapi.entity.RunScriptHistory;
import com.example.datahandlerapi.entity.Script;
import com.example.datahandlerapi.mapper.ScriptMapper;
import com.example.datahandlerapi.repository.GroupRepository;
import com.example.datahandlerapi.repository.RunScriptHistoryRepository;
import com.example.datahandlerapi.repository.ScriptRepository;
import com.example.datahandlerapi.service.ScriptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ScriptServiceImplement implements ScriptService {

    static String UPLOAD_DIR = "/data/scripts";
    ScriptRepository scriptRepository;
    RunScriptHistoryRepository runScriptHistoryRepository;
    ScriptMapper scriptMapper;
    GroupRepository groupRepository;

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
        try {
            Script script = this.scriptRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Script not found with ID " + id));
            String scriptPath = script.getScriptPath();
            if (scriptPath != null && !scriptPath.isEmpty()) {
                Path filePath = Paths.get(scriptPath);
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }
            } else {
                log.warn("File not found: {}", scriptPath);
            }
            this.scriptRepository.delete(script);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete script");
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

    @Override
    public Resource downloadScriptsByGroupName(String groupName) throws IOException {
        List<Script> scripts = this.scriptRepository.findByGroupGroupName(groupName);
        
        if (scripts.isEmpty()) {
            throw new IllegalArgumentException("No scripts found for group: " + groupName);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (Script script : scripts) {
                String scriptPath = script.getScriptPath();
                if (scriptPath != null && !scriptPath.isEmpty()) {
                    Path filePath = Paths.get(scriptPath);
                    if (Files.exists(filePath)) {
                        String fileName = script.getName() != null ? script.getName() : filePath.getFileName().toString();
                        // Ensure the file has an appropriate extension
                        if (!fileName.contains(".")) {
                            fileName += getFileExtension(filePath.toString());
                        }
                        
                        ZipEntry zipEntry = new ZipEntry(fileName);
                        zos.putNextEntry(zipEntry);
                        
                        Files.copy(filePath, zos);
                        zos.closeEntry();
                    } else {
                        log.warn("Script file not found: {} for script: {}", scriptPath, script.getName());
                    }
                }
            }
        }
        return new ByteArrayResource(baos.toByteArray());
    }

    @Override
    public List<Resource> getScriptsResourceByGroupNames(String groupName) {
        Group group = this.groupRepository.findGroupByGroupName(groupName)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with group name " + groupName));

        List<Script> scripts = this.scriptRepository.findByGroupGroupName(groupName);
        if (scripts.isEmpty()) {
            throw new IllegalArgumentException("No scripts found for group: " + groupName);
        }

        return scripts.stream()
                .map(s -> new FileSystemResource(Paths.get(s.getScriptPath()).toFile()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScriptDTO> getScriptsByGroupNames(String groupName) {
        Group group = this.groupRepository.findGroupByGroupName(groupName)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with group name " + groupName));

        List<Script> scripts = this.scriptRepository.findByGroupGroupName(groupName);
        if(scripts.isEmpty()) {
            throw new IllegalArgumentException("Group " + groupName + " had not contained any script yet !");
        }
        List<ScriptDTO> scriptDTOList = scripts.stream().map(this.scriptMapper::toDTO).toList();
        return scriptDTOList;
    }

    @Override
    public RunScriptHistoryDTO updateRunScriptHistory(RunScriptHistoryDTO dto) {
        RunScriptHistory entity = RunScriptHistory.builder()
                .username(dto.getUsername())
                .groupName(dto.getGroupName())
                .status(dto.getStatus())
                .runTime(dto.getRunTime())
                .build();

        RunScriptHistory saved = this.runScriptHistoryRepository.save(entity);
        return new ObjectMapper().convertValue(saved, RunScriptHistoryDTO.class);
    }

    @Override
    public Page<RunScriptHistoryDTO> showScriptHistoryList(CommonFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() != null ? request.getPage().intValue() : 0
                                            ,request.getSize() != null ? request.getSize().intValue() : 10
                                            ,Sort.by(Sort.Direction.DESC, "runTime"));

        Specification<RunScriptHistory> spec = buildSpecification(request);
        Page<RunScriptHistory> historyPage = runScriptHistoryRepository.findAll(spec, pageable);

        return historyPage.map(this::convertToDto);
    }
    private RunScriptHistoryDTO convertToDto(RunScriptHistory entity) {
        RunScriptHistoryDTO dto = new RunScriptHistoryDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setGroupName(entity.getGroupName());
        dto.setStatus(entity.getStatus());
        dto.setRunTime(entity.getRunTime());
        // Map các trường khác nếu có
        return dto;
    }

    private String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filePath.length() - 1) {
            return filePath.substring(lastDotIndex);
        }
        return ""; // No extension found
    }

    private Specification<RunScriptHistory> buildSpecification(CommonFilterRequest request) {
        return((root, query, criteriaBuilder) -> {
          List<Predicate> predicates = new ArrayList<>();
          //filter by username
            if(StringUtils.hasText(request.getUserName())){
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("username")),
                        "%" + request.getUserName() + "%" ));
            }

          //filter by group name
          if(StringUtils.hasText(request.getGroupName())){
              predicates.add(criteriaBuilder.like(
                      criteriaBuilder.lower(root.get("groupName")),
                      "%" + request.getGroupName() + "%" ));
          }

          if(StringUtils.hasText(request.getSearchName())) {
              String searchTerm = "%" + request.getSearchName().toLowerCase() + "%";
              Predicate userNamePredicate = criteriaBuilder.like(
                      criteriaBuilder.lower(root.get("username")),searchTerm);

              Predicate groupNamePredicate = criteriaBuilder.like(
                      criteriaBuilder.lower(root.get("groupName")),searchTerm);
          }
          return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }


}

package com.example.datahandlerapi.controller;

import com.example.datahandlerapi.dto.ScriptDTO;
import com.example.datahandlerapi.dto.request.ScriptRequest;
import com.example.datahandlerapi.dto.response.ApiResponse;
import com.example.datahandlerapi.entity.Group;
import com.example.datahandlerapi.entity.Script;
import com.example.datahandlerapi.mapper.ScriptMapper;
import com.example.datahandlerapi.repository.GroupRepository;
import com.example.datahandlerapi.repository.ScriptRepository;
import com.example.datahandlerapi.service.ScriptService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/script")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScriptController {

    ScriptService scriptService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ScriptDTO>> uploadScript(
            @RequestParam("file") MultipartFile file,
            @RequestParam("scriptName") String scriptName,
            @RequestParam("description") String description,
            @RequestParam("createdBy") String createdBy,
            @RequestParam("groupName") String groupName)
    {
        ScriptRequest request = new ScriptRequest(scriptName, description, groupName);
        ScriptDTO script = scriptService.uploadScript(file, request, createdBy);
        return ResponseEntity.ok(
                ApiResponse.<ScriptDTO>builder()
                        .status("success")
                        .message("Upload script successfully")
                        .data(script)
                        .build()
        );
    }

//    @GetMapping("/downloadAll")
//    public ResponseEntity<?> downloadAllScript(@RequestParam("username") String username){
//        try{
//            List<Script> allScript = scriptRepository.findAll();
//            if (allScript.isEmpty()){
//                return ResponseEntity.ok("No script found");
//            }
//
//            // destination user folder
//            Path userFolder = Paths.get(UPLOAD_DIR, username);
//            if(!Files.exists(userFolder)){
//                Files.createDirectories(userFolder);
//            }
//            for(Script script : allScript) {
//                Path resourcePath = Paths.get(script.getDescription());
//                if(!Files.exists(resourcePath)){
//                    continue;
//                }
//                Path targetPath  = userFolder.resolve(resourcePath.getFileName());
//                Files.copy(resourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
//            }
//            return ResponseEntity.ok("✅ All scripts copied to user folder: " + userFolder.toAbsolutePath());
//
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error copying script: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/get-all")
//    public ResponseEntity<?> getAllScripts(){
//        try {
//            List<Script> allScript = scriptRepository.findAll();
//            if (allScript.isEmpty()) {
//                return ResponseEntity.ok("No script found");
//            }
//            return ResponseEntity.ok(allScript);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve script: " + e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> deleteScript(@PathVariable Long id) {
//        try {
//            if (!scriptRepository.existsById(id)) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("Script not found");
//            }
//            scriptRepository.deleteById(id);
//            return ResponseEntity.ok("Script deleted successfully");
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to delete script: " + e.getMessage());
//        }
//    }
//
//    @PutMapping("/edit/{id}")
//    public ResponseEntity<?> editScript(
//            @PathVariable Long id,
//            @RequestParam(value = "file", required = false) MultipartFile file,
//            @RequestParam("scriptName") String scriptName,
//            @RequestParam("description") String description,
//            @RequestParam("groupType") String groupType
//    ) {
//        try {
//            Optional<Script> optionalScript = scriptRepository.findById(id);
//            if (optionalScript.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Script not found");
//            }
//
//            Script script = optionalScript.get();
//
//            // Nếu có file mới → ghi đè file cũ
//            if (file != null && !file.isEmpty()) {
//                Path uploadPath = Paths.get(UPLOAD_DIR);
//                if (!Files.exists(uploadPath)) {
//                    Files.createDirectories(uploadPath);
//                }
//
//                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
//                Path destinationPath = uploadPath.resolve(fileName);
//                Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
//
//                // Ghi đè đường dẫn file
//                script.setDestination(destinationPath.toString());
//            }
//
//            // Cập nhật các trường khác
//            script.setName(scriptName);
//            script.setDescription(description);
//            script.setGroupType(groupType);
//            script.setUpdatedBy("admin");
//            script.setUpdatedTime(LocalDateTime.now());
//
//            scriptRepository.save(script);
//            return ResponseEntity.ok(script);
//
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("❌ Lỗi khi cập nhật script: " + e.getMessage());
//        }
//    }

}


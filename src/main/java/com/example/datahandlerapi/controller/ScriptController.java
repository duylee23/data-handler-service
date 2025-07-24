package com.example.datahandlerapi.controller;

import com.example.datahandlerapi.dto.ScriptDTO;
import com.example.datahandlerapi.dto.response.ApiResponse;
import com.example.datahandlerapi.service.ScriptService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/script")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScriptController {

    ScriptService scriptService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ScriptDTO>> uploadScript(
            @ModelAttribute ScriptDTO dto,
            @RequestParam("file") MultipartFile file)
    {
        ScriptDTO script = scriptService.uploadScript(file, dto);
        return ResponseEntity.ok(
                ApiResponse.<ScriptDTO>builder()
                        .status("success")
                        .message("Upload script successfully")
                        .data(script)
                        .build()
        );
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<ScriptDTO>>> getAllScripts(){
        List<ScriptDTO> scriptList = this.scriptService.getScriptList();
        return ResponseEntity.ok(
                ApiResponse.<List<ScriptDTO>>builder()
                        .status("success")
                        .message("Get script list successfully")
                        .data(scriptList)
                        .build()
        );
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteScript(@PathVariable Long id) {
       this.scriptService.deleteScript(id);
       return ResponseEntity.ok(
               ApiResponse.<Void>builder()
                       .status("success")
                       .message("Delete script successfully! ")
                       .build()
       );
    }

    @GetMapping("/dropdown")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getScriptDropdown() {
        List<Map<String, Object>> result = this.scriptService.getScriptDropdownData();
        return ResponseEntity.ok(
                ApiResponse.<List<Map<String, Object>>>builder()
                        .status("success")
                        .message("Get script dropdown successfully")
                        .data(result)
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


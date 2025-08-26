package com.example.datahandlerapi.controller;
import com.example.datahandlerapi.dto.ScriptDTO;
import com.example.datahandlerapi.dto.response.ApiResponse;
import com.example.datahandlerapi.service.ScriptService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/script")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScriptController {

    ScriptService scriptService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ScriptDTO>> uploadScript(
            @ModelAttribute ScriptDTO dto,
            @RequestParam("file") MultipartFile file
    ) {
        ScriptDTO script = scriptService.uploadScript(file, dto);
        return ResponseEntity.ok(ApiResponse.success("Script uploaded successfully", script));
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<ScriptDTO>>> getAllScripts() {
        List<ScriptDTO> scriptList = scriptService.getScriptList();
        return ResponseEntity.ok(ApiResponse.success("Script list retrieved successfully", scriptList));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteScript(@PathVariable Long id) {
        scriptService.deleteScript(id);
        return ResponseEntity.ok(ApiResponse.success("Script delete successfully"));
    }

    @GetMapping("/dropdown")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getScriptDropdown() {
        List<Map<String, Object>> result = scriptService.getScriptDropdownData();
        return ResponseEntity.ok(ApiResponse.success("Script dropdown data retrieved successfully", result));
    }

    @GetMapping("/download-by-group")
    public ResponseEntity<Resource> downloadScriptsByGroupName(@RequestParam("groupName") String groupName) {
        try {
            Resource resource = scriptService.downloadScriptsByGroupName(groupName);
            
            String fileName = groupName + "_scripts.zip";
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
                    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}


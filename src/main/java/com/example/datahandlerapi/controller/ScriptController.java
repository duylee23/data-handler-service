package com.example.datahandlerapi.controller;
import com.example.datahandlerapi.dto.RunScriptHistoryDTO;
import com.example.datahandlerapi.dto.ScriptDTO;
import com.example.datahandlerapi.dto.request.CommonFilterRequest;
import com.example.datahandlerapi.dto.response.ApiResponse;
import com.example.datahandlerapi.service.ScriptService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@RestController
@RequestMapping("/api/script")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
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

    @GetMapping("/script-group")
    public ResponseEntity<ApiResponse<List<ScriptDTO>>> getScriptsByGroupName(@RequestParam String groupName) {
        List<ScriptDTO> data = scriptService.getScriptsByGroupNames(groupName);
        if (data.isEmpty()) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.success("No scripts", data));
        }
        return ResponseEntity.ok(ApiResponse.success("Script list retrieved successfully", data));
    }

    @PostMapping("/download")
    public ResponseEntity<Resource> downloadScripts(@RequestParam("groupName") String groupName)throws IOException {
        List<Resource> resources = scriptService.getScriptsResourceByGroupNames(groupName);

        if (resources.size() == 1) {
            // Nếu chỉ có 1 file → trả trực tiếp
            Resource resource = resources.get(0);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            // Nếu nhiều file → zip lại
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                for (Resource res : resources) {
                    ZipEntry entry = new ZipEntry(res.getFilename());
                    zos.putNextEntry(entry);
                    res.getInputStream().transferTo(zos);
                    zos.closeEntry();
                }
            }

            ByteArrayResource zipResource = new ByteArrayResource(baos.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"scripts.zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(zipResource);
        }

    }

    @PostMapping("/history")
    public ResponseEntity<ApiResponse<RunScriptHistoryDTO>> updateRunScriptHistory(@RequestBody RunScriptHistoryDTO req){
        RunScriptHistoryDTO data = this.scriptService.updateRunScriptHistory(req);
        return ResponseEntity.ok(ApiResponse.success("Script history created successfully", data));
    }

    @PostMapping("history-list")
    public ResponseEntity<ApiResponse<Page<RunScriptHistoryDTO>>> showScriptHistoryList(
            @RequestBody @Validated CommonFilterRequest request) {

        Page<RunScriptHistoryDTO> data = this.scriptService.showScriptHistoryList(request);
        return ResponseEntity.ok(ApiResponse.success("Script history fetched", data));
    }





}


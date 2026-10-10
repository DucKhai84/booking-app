package com.devteria.file.controller;

import com.devteria.file.dto.request.ApiResponse;
import com.devteria.file.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {
    FileService file_service;

    @PostMapping("/media/upload")
    ApiResponse<Object> uploadFile(@RequestParam("file") MultipartFile file){
        return ApiResponse.builder()
                .result(file_service.uploadFile(file))
                .build();
    }
}

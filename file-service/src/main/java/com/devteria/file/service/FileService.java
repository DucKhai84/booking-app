package com.devteria.file.service;

import com.devteria.file.exception.AppException;
import com.devteria.file.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    @Value("${app.file-service.storage-dir}")
    private String storageDir;

    public Object uploadFile(MultipartFile file) {

        Path folder = Paths.get(storageDir);

        String fileNameExtension = StringUtils
                .getFilenameExtension(file.getOriginalFilename());


        var fileName = Objects.isNull(fileNameExtension)
                ? UUID.randomUUID().toString()
                : UUID.randomUUID() + "." + fileNameExtension;

        Path filePath = folder
                .resolve(fileName)
                .normalize()
                .toAbsolutePath();

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(
                    inputStream,
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }catch(IOException e){
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        return fileName;
    }
}

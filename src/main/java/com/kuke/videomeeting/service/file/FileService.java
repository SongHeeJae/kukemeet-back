package com.kuke.videomeeting.service.file;

import com.kuke.videomeeting.model.dto.file.FileUploadRequestDto;
import com.kuke.videomeeting.model.dto.file.FileUploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public interface FileService {

    FileUploadResponseDto uploadFile(FileUploadRequestDto uploadDto);

    void deleteFilesInDirectory(String path);

    default File convertMultipartFileToFile(MultipartFile mFile, String tempPath) throws IOException {
        File file = new File("src/main/resources/" + tempPath);
        if (file.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(mFile.getBytes());
            }
            return file;
        }
        throw new IOException();
    }

    default void removeFile(File file) {
        file.delete();
    }
}

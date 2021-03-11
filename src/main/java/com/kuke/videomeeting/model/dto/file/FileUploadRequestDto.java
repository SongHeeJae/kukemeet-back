package com.kuke.videomeeting.model.dto.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileUploadRequestDto {
    @NotNull
    private MultipartFile file;

    @NotBlank
    private String transaction;

    @NotBlank
    private String room;
}

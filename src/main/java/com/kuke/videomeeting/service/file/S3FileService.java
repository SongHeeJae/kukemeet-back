package com.kuke.videomeeting.service.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.kuke.videomeeting.advice.exception.FileUploadFailureException;
import com.kuke.videomeeting.model.dto.file.FileUploadRequestDto;
import com.kuke.videomeeting.model.dto.file.FileUploadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RequiredArgsConstructor
@Service
public class S3FileService implements FileService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.baseUrl}")
    private String baseUrl;

    @Override
    public FileUploadResponseDto uploadFile(FileUploadRequestDto requestDto) {
        try {
            MultipartFile file = requestDto.getFile();
            String transaction = requestDto.getTransaction();
            String filename = requestDto.getFile().getOriginalFilename();
            String key = requestDto.getRoom() + "/" + transaction + "/" + filename;
            File convertedFile = convertMultipartFileToFile(file, transaction + filename);
            TransferManager transferManager = TransferManagerBuilder
                    .standard()
                    .withS3Client(amazonS3)
                    .withMultipartUploadThreshold(300L * 1024 * 1024)
                    .build();
            Upload upload = transferManager.upload(bucket, key, convertedFile);
            upload.waitForUploadResult();
            removeFile(convertedFile);
            return new FileUploadResponseDto(baseUrl + key);
        } catch(Exception e) {
            throw new FileUploadFailureException();
        }
    }

    @Override
    public void deleteFilesInDirectory(String path) {
        for (S3ObjectSummary file : amazonS3.listObjects(bucket, path).getObjectSummaries()) {
            amazonS3.deleteObject(bucket, file.getKey());
        }
    }
}

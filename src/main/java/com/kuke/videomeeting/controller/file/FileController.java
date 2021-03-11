package com.kuke.videomeeting.controller.file;

import com.kuke.videomeeting.model.dto.file.FileUploadRequestDto;
import com.kuke.videomeeting.model.dto.response.Result;
import com.kuke.videomeeting.service.common.ResponseService;
import com.kuke.videomeeting.service.file.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "File Controller", tags = {"File"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    private final ResponseService responseService;
    private final FileService fileService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping(value = "/files")
    public Result uploadFile(@Valid @ModelAttribute FileUploadRequestDto requestDto) {
        return responseService.getSingleResult(fileService.uploadFile(requestDto));
    }

//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "access-token", required = true, dataType = "String", paramType = "header")
//    })
//    @DeleteMapping(value = "/files/{filename}")
//    public Result deleteFile(@PathVariable("filename") String filename) {
//        fileService.deleteFilesInDirectory(filename);
//        return responseService.getSuccessResult();
//    }



}

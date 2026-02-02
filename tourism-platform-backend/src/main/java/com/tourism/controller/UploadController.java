package com.tourism.controller;

import com.tourism.utils.FileUtil;
import com.tourism.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/upload")
@Api(tags = "文件上传")
public class UploadController {
    
    @Autowired
    private FileUtil fileUtil;
    
    @PostMapping("/image")
    @ApiOperation("上传图片")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件类型
            if (!fileUtil.isValidImageType(file.getOriginalFilename())) {
                return Result.error("不支持的文件类型，仅支持jpg、jpeg、png、gif、bmp、webp格式");
            }
            
            String url = fileUtil.uploadFile(file, "images");
            Map<String, String> result = new HashMap<>();
            result.put("url", url);
            return Result.success("上传成功", result);
        } catch (IOException e) {
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/video")
    @ApiOperation("上传视频")
    public Result<Map<String, String>> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件类型
            if (!fileUtil.isValidVideoType(file.getOriginalFilename())) {
                return Result.error("不支持的文件类型，仅支持mp4、avi、mov、wmv、flv、mkv格式");
            }
            
            String url = fileUtil.uploadFile(file, "videos");
            Map<String, String> result = new HashMap<>();
            result.put("url", url);
            return Result.success("上传成功", result);
        } catch (IOException e) {
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }
}


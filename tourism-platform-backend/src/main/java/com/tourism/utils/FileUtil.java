package com.tourism.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件工具类
 */
@Component
public class FileUtil {
    
    @Value("${file.upload.path:./uploads/}")
    private String uploadPath;
    
    @Value("${file.upload.max-size}")
    private Long maxSize;
    
    private String resolvedUploadPath;
    
    /**
     * 初始化时解析上传路径，将相对路径转换为绝对路径
     */
    @PostConstruct
    public void init() {
        // 如果路径是相对路径，转换为绝对路径
        Path path = Paths.get(uploadPath);
        if (!path.isAbsolute()) {
            // 获取项目根目录（jar包所在目录或工作目录）
            String baseDir = System.getProperty("user.dir");
            resolvedUploadPath = Paths.get(baseDir, uploadPath).normalize().toString();
        } else {
            resolvedUploadPath = uploadPath;
        }
        
        // 确保路径以分隔符结尾
        if (!resolvedUploadPath.endsWith(File.separator)) {
            resolvedUploadPath += File.separator;
        }
        
        // 创建上传目录（如果不存在）
        try {
            Path uploadDir = Paths.get(resolvedUploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("无法创建上传目录: " + resolvedUploadPath, e);
        }
    }
    
    /**
     * 获取解析后的上传路径
     */
    public String getUploadPath() {
        return resolvedUploadPath;
    }
    
    /**
     * 上传文件
     */
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        // 检查文件大小
        if (file.getSize() > maxSize) {
            throw new RuntimeException("文件大小超过限制");
        }
        
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new RuntimeException("文件名不能为空");
        }
        
        // 生成新的文件名
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;
        
        // 创建目录
        String folderPath = resolvedUploadPath + folder;
        Path path = Paths.get(folderPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        
        // 保存文件
        String filePath = folderPath + File.separator + newFilename;
        file.transferTo(new File(filePath));
        
        // 返回访问路径
        return "/uploads/" + folder + "/" + newFilename;
    }
    
    /**
     * 删除文件
     */
    public boolean deleteFile(String filePath) {
        try {
            // 移除路径前缀 /uploads/
            String relativePath = filePath.replace("/uploads/", "").replace("\\uploads\\", "");
            String fullPath = resolvedUploadPath + relativePath;
            File file = new File(fullPath);
            if (file.exists()) {
                return file.delete();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 检查文件类型
     */
    public boolean isValidImageType(String filename) {
        String[] allowedTypes = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"};
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        for (String type : allowedTypes) {
            if (type.equals(extension)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 检查文件类型（视频）
     */
    public boolean isValidVideoType(String filename) {
        String[] allowedTypes = {".mp4", ".avi", ".mov", ".wmv", ".flv", ".mkv"};
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        for (String type : allowedTypes) {
            if (type.equals(extension)) {
                return true;
            }
        }
        return false;
    }
}


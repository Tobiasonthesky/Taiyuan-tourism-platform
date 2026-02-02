package com.tourism.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 自定义日期反序列化器
 * 支持多种日期格式
 */
public class CustomDateDeserializer extends DateDeserializers.DateDeserializer {
    
    private static final String[] DATE_FORMATS = {
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ss.SSS",
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
        "yyyy-MM-dd"
    };
    
    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateStr = p.getText();
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        
        // 尝试多种日期格式
        for (String format : DATE_FORMATS) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                return sdf.parse(dateStr);
            } catch (Exception e) {
                // 继续尝试下一个格式
            }
        }
        
        // 如果所有格式都失败，使用默认解析
        try {
            return super.deserialize(p, ctxt);
        } catch (Exception e) {
            throw new IOException("无法解析日期格式: " + dateStr, e);
        }
    }
}


package com.tourism.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 国际化工具类
 */
@Component
public class I18nUtil {
    
    @Autowired
    private MessageSource messageSource;
    
    /**
     * 获取国际化消息
     */
    public String getMessage(String code) {
        return getMessage(code, (Object[]) null);
    }
    
    /**
     * 获取国际化消息（带参数）
     */
    public String getMessage(String code, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, code, locale);
    }
    
    /**
     * 获取国际化消息（指定语言）
     */
    public String getMessageWithLocale(String code, Locale locale) {
        return messageSource.getMessage(code, null, code, locale);
    }
}


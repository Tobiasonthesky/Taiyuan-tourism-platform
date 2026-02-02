package com.tourism.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.Locale;

/**
 * 国际化配置类
 */
@Configuration
public class I18nConfig {
    
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        localeResolver.setSupportedLocales(Arrays.asList(
                Locale.SIMPLIFIED_CHINESE,
                Locale.US, // 英文
                Locale.JAPANESE, // 日文
                Locale.KOREAN // 韩文
        ));
        return localeResolver;
    }
}


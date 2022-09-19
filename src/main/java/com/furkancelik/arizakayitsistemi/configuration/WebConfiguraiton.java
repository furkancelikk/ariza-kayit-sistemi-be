package com.furkancelik.arizakayitsistemi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfiguraiton implements WebMvcConfigurer {

    @Autowired
    private AppConfiguration appConfiguration;

    //Statik dosylar için path ayarlaması
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:./"+appConfiguration.getUploadPath())
                .setCacheControl(CacheControl.maxAge(24, TimeUnit.HOURS));
    }

    // Sistem başlarken aktif profile göre statik dosyaların tutulacağı klasörün oluşturulması
    @Bean
    CommandLineRunner createUploadFolder(){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                createFolder(appConfiguration.getUploadPath());
                createFolder(appConfiguration.getAttachmentStorePath());
                createFolder(appConfiguration.getProfileStoragePath());
            }
        };
    }

    private void createFolder(String path){
        File folder = new File(path);
        if (!(folder.exists() && folder.isDirectory())){
            folder.mkdir();
        }
    }
}


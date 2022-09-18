package com.furkancelik.arizakayitsistemi.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties
public class AppConfiguration {

    // aktif profile göre properties dosyasından değişken adı ile erişilebilir
    private String uploadPath;

    private String profileStorage = "profile/";

    private String attachmentStorage = "attachment/";

    public String getProfileStoragePath(){
        return this.uploadPath + this.profileStorage;
    }

    public String getAttachmentStorePath(){
        return this.uploadPath + this.attachmentStorage;
    }
}

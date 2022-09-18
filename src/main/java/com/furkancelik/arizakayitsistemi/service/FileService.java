package com.furkancelik.arizakayitsistemi.service;

import com.furkancelik.arizakayitsistemi.configuration.AppConfiguration;
import com.furkancelik.arizakayitsistemi.model.FileAttachment;
import com.furkancelik.arizakayitsistemi.repository.FileAttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@EnableScheduling
public class FileService {

    private FileAttachmentRepository fileAttachmentRepository;

    private AppConfiguration appConfiguration;

    private Tika tika;

    public FileService(AppConfiguration appConfiguration, FileAttachmentRepository fileAttachmentRepository) {
        this.appConfiguration = appConfiguration;
        this.fileAttachmentRepository = fileAttachmentRepository;
        tika = new Tika();
    }

    // base64 tipinde gelen dosyayı resim olarak kaydeder.
    public String base64ToFile(String image) throws IOException {

        String fileName = generateRandomName() + ".png";
        File target = new File(appConfiguration.getProfileStoragePath() + fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(target);

        byte[] byteArr = Base64.getDecoder().decode(image);

        fileOutputStream.write(byteArr);
        fileOutputStream.close();
        return fileName;
    }

    // kaydedilecek her dosya için random bir isim oluşturur, oluşan isim içerisinde bulunan # karakteri çıkarılmaktadır
    private String generateRandomName() {
        return UUID.randomUUID().toString().replaceAll("#", "");
    }

    // eğer verilen isimde dosya yoksa return eder varsa aktif profildeki pathe göre dosyası siler hata fırlatmaz
    public void removeProfileImage(String name){
        if (name == null) return;
        Path path = Paths.get(appConfiguration.getProfileStoragePath(), name);
        this.removeFile(path);
    }
    public void removeAttachment(String name){
        if (name == null) return;
        Path path = Paths.get(appConfiguration.getAttachmentStorePath(), name);
        this.removeFile(path);
    }
    public void removeFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Tika bağımlılığı ile gelen base64 tipindeki dosyanın türü belirlenir ve return eder
    public String detectType(String value) {
        byte[] byteArr = Base64.getDecoder().decode(value);
        return tika.detect(byteArr);
    }
    public String detectType(byte[] arr) {
        return tika.detect(arr);
    }

    public FileAttachment savePostAttachment(MultipartFile file) throws IOException {
        String fileName = generateRandomName() + ".png";
        File target = new File(appConfiguration.getAttachmentStorePath() + fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(target);

        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();

        FileAttachment attachment = new FileAttachment();
        attachment.setName(fileName);
        String type = this.detectType(file.getBytes());
        attachment.setFileType(type);
        return fileAttachmentRepository.save(attachment);
    }

    // 24 saatte bir çalışır ve bir post ile ilişkisi olmayan dosyaları siler
    //24 * 60 * 60 * 1000  --=> 24 saat
    //10 * 1000 --=> 10 saniye
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void cleanUpStorage() {
        Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // 24 saatin milisaniye cinsi
        List<FileAttachment> attachmentList = fileAttachmentRepository.findByDateBeforeAndPostIsNull(twentyFourHoursAgo);

        for (FileAttachment attachment : attachmentList) {
            this.removeAttachment(attachment.getName());
            fileAttachmentRepository.deleteById(attachment.getId());
        }
    }
}

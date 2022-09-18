package com.furkancelik.arizakayitsistemi.validator;

import com.furkancelik.arizakayitsistemi.annotation.FileType;
import com.furkancelik.arizakayitsistemi.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Base64;

public class FileTypeValidator implements ConstraintValidator<FileType, String> {

    @Autowired
    private FileService fileService;
    private String[] types;

    // oluşturduğumuz anotasyon içerisindeki type fonksiyonuna ulaşmak için kullanıyoruz
    @Override
    public void initialize(FileType constraintAnnotation) {
        types = constraintAnnotation.types();
    }

    // field tanımı sırasında verilen typelara göre validasyon uygular
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) return true;

        String fileType = fileService.detectType(value);
        for (String supportedType : types) {
            if (fileType.equalsIgnoreCase(supportedType)) return true;
        }

        return false;
    }
}

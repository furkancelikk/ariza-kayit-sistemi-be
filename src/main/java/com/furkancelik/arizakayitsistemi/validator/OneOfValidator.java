package com.furkancelik.arizakayitsistemi.validator;

import com.furkancelik.arizakayitsistemi.annotation.OneOf;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OneOfValidator implements ConstraintValidator<OneOf, Object> {

    private String[] values;

    // oluşturduğumuz anotasyon içerisindeki type fonksiyonuna ulaşmak için kullanıyoruz
    @Override
    public void initialize(OneOf constraintAnnotation) {
        values = constraintAnnotation.values();
    }

    // field tanımı sırasında verilen typelara göre validasyon uygular
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return false;

        for (String val : values) {
            if (val.equals(value.toString()))
                return true;
        }
        return false;
    }
}

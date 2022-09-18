package com.furkancelik.arizakayitsistemi.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ PARAMETER})
@Retention(RUNTIME)
@AuthenticationPrincipal // principaldan usera cast işlemini burada yapıyoruz. -> bknz: AuthController
public @interface CurrentUser {
}

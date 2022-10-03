package com.furkancelik.arizakayitsistemi.configuration;

import com.furkancelik.arizakayitsistemi.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecutityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthEntryPoint()); // eğer bu olmazsa tarayıcıda login popup çıkar

//        http.authorizeRequests()
//                .antMatchers(HttpMethod.POST,"/api/1.0/auth}").authenticated()
//                .antMatchers(HttpMethod.DELETE,"/api/1.0/user/{username}").authenticated()
//                .antMatchers(HttpMethod.POST,"/api/1.0/posts").authenticated()
//                .antMatchers(HttpMethod.POST,"/api/1.0/file/postAttachment").authenticated()
//                .antMatchers(HttpMethod.DELETE,"/api/1.0/posts/{id}").authenticated()
//                .antMatchers(HttpMethod.POST, "/api/1.0/logout").authenticated()
//                .antMatchers(HttpMethod.POST, "/api/1.0/categories").authenticated()
//                .and()
//                .authorizeRequests().anyRequest().permitAll();
        http.authorizeRequests()
                        .antMatchers(HttpMethod.POST, "/api/1.0/auth").permitAll()
                        .antMatchers(HttpMethod.GET, "/images/**").permitAll()
                        .antMatchers( "/api/1.0/**").authenticated();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class); // bizim filtemizin önce çalışmasını sağlar

    }

    @Bean
    TokenFilter tokenFilter(){
        return new TokenFilter();
    }
}

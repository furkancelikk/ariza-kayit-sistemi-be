package com.furkancelik.arizakayitsistemi;

import com.furkancelik.arizakayitsistemi.enums.UserRole;
import com.furkancelik.arizakayitsistemi.model.User;
import com.furkancelik.arizakayitsistemi.repository.PostRepository;
import com.furkancelik.arizakayitsistemi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication //(exclude = SecurityAutoConfiguration.class)
@EnableSwagger2
public class ArizaKayitSistemiBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArizaKayitSistemiBeApplication.class, args);
    }

    @Bean
//    @Profile("dev") sadece devde çalış
//    @Profile("!prod") production haricinde çalış
    CommandLineRunner createInitalUser(UserRepository userRepository, PostRepository postRepository){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                User inDb = userRepository.findByUsername("admin");
                if (inDb == null){
                    User user = new User();
                    user.setUsername("admin");
                    user.setDisplayName("ADMIN");
                    user.setPassword(new BCryptPasswordEncoder().encode("12345"));
                    user.setRole(UserRole.ADMIN);
                    userRepository.save(user);
                }

//                for (int i=0; i<5; i++){
//                    User user = new User();
//                    user.setUsername("kullanıcı"+i);
//                    user.setPassword(new BCryptPasswordEncoder().encode("12345"));
//                    user.setDisplayName("kullanıcı display name"+i);
//                    User inDB = userRepository.save(user);
//                    for (int k=0; k<5; k++){
//                        Post post = new Post();
//                        post.setContent("post " + k + " user " + i);
//                        post.setUser(inDB);
//                        postRepository.save(post);
//                    }
//                }
            }
        };
    }
}


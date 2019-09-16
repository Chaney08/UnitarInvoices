package org.unitar.invoices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.unitar.invoices.model.User;
import org.unitar.invoices.repository.UserRepository;

import java.util.stream.Stream;

@SpringBootApplication
@ComponentScan(basePackages={"org.unitar.invoices.controller", "org.unitar.invoices.config","org.unitar.invoices.service","org.unitar.invoices.repository"
        ,"org.unitar.invoices.utils"})
public class UnitarInvoiceApplication {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public static void main(String[] args) {
        SpringApplication.run(UnitarInvoiceApplication.class, args);
    }

    //This just creates base users for testing, add as many users as required to array
    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            Stream.of("user1").forEach(name -> {
                User user = new User(name, passwordEncoder.encode("123"),name);
                userRepository.save(user);
            });
            userRepository.findAll().forEach(System.out::println);
        };
    }
}

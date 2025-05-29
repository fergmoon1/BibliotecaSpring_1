package edu.sena.bibliotecaspring_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "edu.sena.bibliotecaspring_1.model")
public class BibliotecaSpring1Application {
    public static void main(String[] args) {
        SpringApplication.run(BibliotecaSpring1Application.class, args);
    }
}
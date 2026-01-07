package com.libromanager.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "LibroManager API",
                version = "1.0",
                description = "Documentation for Library Management System MVP. Allows management of books, authors, publishers, readers, loands and reviews."
        )
)
public class LibroManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibroManagerApplication.class, args);
    }
}

package com.prestobr.fiscal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FiscalApplication {

    // Metodo main — inicia o servidor Tomcat embutido e sobe toda a aplicação Spring Boot
    public static void main(String[] args) {
        SpringApplication.run(FiscalApplication.class, args);
    }
}

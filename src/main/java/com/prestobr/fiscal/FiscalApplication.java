package com.prestobr.fiscal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação — ponto de entrada do sistema.
 *
 * @SpringBootApplication combina três anotações em uma só:
 *
 *   @Configuration        → esta classe pode declarar beans (objetos gerenciados pelo Spring)
 *   @EnableAutoConfiguration → o Spring configura automaticamente as libs encontradas no projeto
 *                              (ex: encontrou Jackson? já configura JSON. Encontrou Tomcat? sobe o servidor.)
 *   @ComponentScan        → varre todos os pacotes abaixo de "com.prestobr.fiscal" procurando
 *                              classes com @Service, @Controller, @Repository, @Component, etc.
 */
@SpringBootApplication
public class FiscalApplication {

    /**
     * Método main — inicia o servidor Tomcat embutido e sobe toda a aplicação Spring Boot.
     * Após a execução, a API já está disponível em http://localhost:8080
     */
    public static void main(String[] args) {
        SpringApplication.run(FiscalApplication.class, args);
    }
}

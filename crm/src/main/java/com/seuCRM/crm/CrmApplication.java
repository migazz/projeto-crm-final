package com.seuCRM.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication diz ao Spring: "carregue tudo automaticamente"
// Ela combina três anotações: @Configuration, @EnableAutoConfiguration, @ComponentScan
@SpringBootApplication
public class CrmApplication {

    public static void main(String[] args) {
        // Inicia o servidor embutido (Tomcat) e sobe a aplicação
        SpringApplication.run(CrmApplication.class, args);
    }

}
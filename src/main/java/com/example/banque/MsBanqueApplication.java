package com.example.banque;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MsBanqueApplication {
    public static void main(String[] args) { SpringApplication.run(MsBanqueApplication.class, args); }

    @Bean
    CommandLineRunner start(CompteRepository repository) {
        return args -> {
            repository.save(new Compte(null, ThreadLocalRandom.current().nextDouble(9000), new Date(), TypeCompte.EPARGNE));
            repository.save(new Compte(null, ThreadLocalRandom.current().nextDouble(9000), new Date(), TypeCompte.COURANT));
            repository.save(new Compte(null, ThreadLocalRandom.current().nextDouble(9000), new Date(), TypeCompte.EPARGNE));
            repository.findAll().forEach(c -> System.out.println("Compte " + c.getId() + " : " + c.getSolde()));
        };
    }
}

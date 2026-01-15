package com.mmebaovola.taxibrousse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan("com.mmebaovola.taxibrousse.entity")
@EnableScheduling
public class TaxiBrousseApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaxiBrousseApplication.class, args);
    }
}

package com.mmebaovola.taxibrousse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.mmebaovola.taxibrousse.entity")
public class TaxiBrousseApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaxiBrousseApplication.class, args);
    }
}

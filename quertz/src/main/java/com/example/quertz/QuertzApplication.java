package com.example.quertz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class QuertzApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuertzApplication.class, args);
    }



}

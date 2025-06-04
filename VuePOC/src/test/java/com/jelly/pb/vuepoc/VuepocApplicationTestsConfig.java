package com.jelly.pb.vuepoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.jelly.pb.vuepoc.CustomBeanNameGenerator;
@SpringBootApplication
@ComponentScan(nameGenerator = CustomBeanNameGenerator.class)
public class VuepocApplicationTestsConfig {

    public static void main(String[] args) {
        SpringApplication.run(VuepocApplicationTestsConfig.class, args);
    }
    
}
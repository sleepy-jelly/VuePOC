package com.jelly.pb.vuepoc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(nameGenerator = CustomBeanNameGenerator.class)
@MapperScan("com.jelly.pb.common.user.service.impl")
@SpringBootApplication
public class VuepocApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(VuepocApplication.class, args);
	}

}

package com.app.expd;

import com.app.expd.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class LetsTalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(LetsTalkApplication.class, args);
	}

}

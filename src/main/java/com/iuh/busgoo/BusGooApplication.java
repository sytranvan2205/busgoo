package com.iuh.busgoo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BusGooApplication {
	
	@Value("${server.base.path}")
    private String basePath;
	
//	@Bean(name = "multipartResolver")
//    public CommonsMultipartResolver multipartResolver() {
//        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//        resolver.setDefaultEncoding("utf-8");
//        resolver.setMaxUploadSizePerFile(10 * 1024 * 1024); // Giới hạn dung lượng tối đa của file
//        return resolver;
//    }

    @Bean
    public String basePath() {
        return basePath;
    }

	public static void main(String[] args) {
		SpringApplication.run(BusGooApplication.class, args);
	}

}

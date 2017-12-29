package com.dvtrsc.codevault.cvbe.ecosystem;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import io.swagger.annotations.Info;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/*
 * SwaggerConfig. Swagger online documentation configuration 
 * D.Tordera, 20171031
 * 
 */

@Configuration
@EnableSwagger2

public class SwaggerConfig {
	
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)
          .host("vps355126.ovh.net:8081") // production
//          .host("localhost:8080") // develop
          .pathMapping("/")
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("com.dvtrsc.codevault.cvbe.controllers"))             
          .paths(PathSelectors.any())                          
          .build()          
          .apiInfo(getApiInfo());                                          
    }        
    
    @SuppressWarnings("rawtypes")
	private ApiInfo getApiInfo()
    {
    	return new ApiInfo(
    			"COM.DVTRSC.CODEVAULT.CVBE",	
				"David's Code Vault microservices",
				"v0.2",
				"",
				new Contact("D.Tordera","https://davidscodevault.com","dtordera@gmail.com"),
				"","",
				new ArrayList<VendorExtension>());				
    }
}
package com.example.demo;

import com.azure.core.credential.TokenCredential;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

        System.out.println("Run git is done.");


	}

    @RequestMapping("/get")
    public String get() {

    TokenRequestContext request = new TokenRequestContext().addScopes("https://management.azure.com/.default");

        TokenCredential credential = new DefaultAzureCredentialBuilder()
            .build();

        System.out.println(credential.getTokenSync(request).getToken());
        return "Hello";
    }

}

package com.whisky.blogrestapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class SwaggerConfig {

	@Value("${app.module-name}")
	private String moduleName;
	@Value("${app.api-version}")
	private String apiVersion;

	@Bean
	public OpenAPI customOpenAPI() {
		final String securitySchemeName = "bearerAuth";
		final String apiTitle = String.format("%s API", StringUtils.capitalize(moduleName));
		return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
				.components(new Components().addSecuritySchemes(securitySchemeName,
						new SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP)
								.scheme("bearer").bearerFormat("JWT")))
				.info(new Info().title(apiTitle)
						.description("Spring Boot Blog REST API Documentation")
						.termsOfService("Term of Service").contact(getContact())
						.license(getLicense()).version(apiVersion));
	}

	private Contact getContact() {
		Contact contact = new Contact();
		contact.setName("Peerawit Montriswetkul");
		contact.setUrl("www.google.con");
		contact.setEmail("Mr.peerawit@live.com");

		return contact;
	}

	private License getLicense() {
		License license = new License();
		license.setName("License of API");
		license.setUrl("www.google.com");

		return license;
	}
}
package com.segg3r.learning.camel;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Servlet;

@SpringBootApplication
public class CamelApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamelApplication.class, args);
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		CamelHttpTransportServlet camelHttpTransportServlet = new CamelHttpTransportServlet();
		camelHttpTransportServlet.setAsync(true);

		ServletRegistrationBean registration = new ServletRegistrationBean
				(camelHttpTransportServlet, "/api/*");
		registration.setName("CamelServlet");

		return registration;
	}

}

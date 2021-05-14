package com.segg3r.learning.camel;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.quartz.SchedulerException;
import org.quartz.core.QuartzScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@SpringBootApplication
@EnableCaching
public class CamelApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamelApplication.class, args);
	}

	@Bean
	public ServletRegistrationBean<CamelHttpTransportServlet> servletRegistrationBean() {
		CamelHttpTransportServlet camelHttpTransportServlet = new CamelHttpTransportServlet();
		camelHttpTransportServlet.setAsync(true);

		ServletRegistrationBean<CamelHttpTransportServlet> registration = new ServletRegistrationBean<>
				(camelHttpTransportServlet, "/api/*");
		registration.setName("CamelServlet");

		return registration;
	}

	@Bean(name = "jmsJsonMessageConverter")
	public MappingJackson2MessageConverter mappingJackson2MessageConverter() {
		MappingJackson2MessageConverter mappingJackson2MessageConverter
				= new MappingJackson2MessageConverter();
		mappingJackson2MessageConverter.setTargetType(MessageType.TEXT);
		mappingJackson2MessageConverter.setTypeIdPropertyName("_type");
		return mappingJackson2MessageConverter;
	}

}

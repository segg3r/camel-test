package com.segg3r.learning.camel.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class FlywayTestInitializer implements BeanPostProcessor {

    private static final Logger LOG = LogManager.getLogger(FlywayTestInitializer.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Flyway) {
            Flyway flyway = (Flyway) bean;
            LOG.info("Detected flyway bean " + flyway + ". Cleaning the schema for tests.");

            flyway.clean();
        }

        return bean;
    }

}

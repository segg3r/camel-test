package com.segg3r.learning.camel.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "spring.flyway.reinitialize-schema", havingValue = "true")
public class FlywayCleanup implements BeanPostProcessor {

    private static final Logger LOG = LogManager.getLogger(FlywayCleanup.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Flyway) {
            Flyway flyway = (Flyway) bean;
            LOG.info("Detected flyway bean " + flyway + " and 'flyway.schema.reinitialize-schema' is 'true'. Cleaning the schema.");

            flyway.clean();
        }

        return bean;
    }

}

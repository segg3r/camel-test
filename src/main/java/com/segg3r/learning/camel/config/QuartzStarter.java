package com.segg3r.learning.camel.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class QuartzStarter {

    private final Scheduler scheduler;

    @Autowired
    public QuartzStarter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startQuartzOnCamelStartup(ApplicationReadyEvent event) throws SchedulerException {
        scheduler.start();
    }
}

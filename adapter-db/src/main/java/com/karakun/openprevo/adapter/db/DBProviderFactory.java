package com.karakun.openprevo.adapter.db;

import ch.prevo.open.node.data.provider.JobEndProvider;
import ch.prevo.open.node.data.provider.JobStartProvider;
import ch.prevo.open.node.data.provider.ProviderFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DBProviderFactory implements ProviderFactory {

    private final JobStartProvider jobStartProvider;
    private final JobEndProvider jobEndProvider;


    public DBProviderFactory() {
        final ApplicationContext context = new AnnotationConfigApplicationContext("com.karakun.openprevo.adapter.db");
        this.jobStartProvider = context.getBean(JobStartProvider.class);
        this.jobEndProvider = context.getBean(JobEndProvider.class);
    }

    @Override
    public JobStartProvider getJobStartProvider() {
        return jobStartProvider;
    }

    @Override
    public JobEndProvider getJobEndProvider() {
        return jobEndProvider;
    }
}

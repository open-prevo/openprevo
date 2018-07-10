package com.karakun.openprevo.adapter.db;

import ch.prevo.open.node.data.provider.JobEndProvider;
import ch.prevo.open.node.data.provider.JobStartProvider;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.ProviderFactory;
import ch.prevo.open.node.data.provider.dummy.DefaultMatchNotificationListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DBProviderFactory implements ProviderFactory {

    private final JobStartProvider jobStartProvider;
    private final JobEndProvider jobEndProvider;
    private final MatchNotificationListener matchNotificationListener = new DefaultMatchNotificationListener();


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

    @Override
    public MatchNotificationListener getMatchNotificationListener() {
        return matchNotificationListener;
    }
}

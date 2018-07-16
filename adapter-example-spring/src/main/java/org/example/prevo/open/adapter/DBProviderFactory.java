package org.example.prevo.open.adapter;

import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.ProviderFactory;
import ch.prevo.open.node.data.provider.dummy.DefaultMatchNotificationListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DBProviderFactory implements ProviderFactory {

    private final EmploymentCommencementProvider jobStartProvider;
    private final EmploymentTerminationProvider jobEndProvider;
    private final MatchNotificationListener matchNotificationListener = new DefaultMatchNotificationListener();


    public DBProviderFactory() {
        final ApplicationContext context = new AnnotationConfigApplicationContext("org.example.prevo.open.adapter");
        this.jobStartProvider = context.getBean(EmploymentCommencementProvider.class);
        this.jobEndProvider = context.getBean(EmploymentTerminationProvider.class);
    }

    @Override
    public EmploymentCommencementProvider getEmploymentCommencementProvider() {
        return jobStartProvider;
    }

    @Override
    public EmploymentTerminationProvider getEmploymentTerminationProvider() {
        return jobEndProvider;
    }

    @Override
    public MatchNotificationListener getMatchNotificationListener() {
        return matchNotificationListener;
    }
}

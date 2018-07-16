package org.example.prevo.open.adapter;

import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.ProviderFactory;
import ch.prevo.open.node.data.provider.dummy.DefaultMatchNotificationListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DBProviderFactory implements ProviderFactory {

    private final EmploymentCommencementProvider employmentStartProvider;
    private final EmploymentTerminationProvider employmentEndProvider;
    private final MatchNotificationListener matchNotificationListener = new DefaultMatchNotificationListener();


    public DBProviderFactory() {
        final ApplicationContext context = new AnnotationConfigApplicationContext("org.example.prevo.open.adapter");
        this.employmentStartProvider = context.getBean(EmploymentCommencementProvider.class);
        this.employmentEndProvider = context.getBean(EmploymentTerminationProvider.class);
    }

    @Override
    public EmploymentCommencementProvider getEmploymentCommencementProvider() {
        return employmentStartProvider;
    }

    @Override
    public EmploymentTerminationProvider getEmploymentTerminationProvider() {
        return employmentEndProvider;
    }

    @Override
    public MatchNotificationListener getMatchNotificationListener() {
        return matchNotificationListener;
    }
}

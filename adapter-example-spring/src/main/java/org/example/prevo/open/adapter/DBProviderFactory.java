package org.example.prevo.open.adapter;

import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.ProviderFactory;
import ch.prevo.open.node.data.provider.dummy.DefaultMatchNotificationListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DBProviderFactory implements ProviderFactory {

    private final EmploymentCommencementProvider employmentCommencementProvider;
    private final EmploymentTerminationProvider employmentTerminationProvider;
    private final MatchNotificationListener matchNotificationListener = new DefaultMatchNotificationListener();


    public DBProviderFactory() {
        final ApplicationContext context = new AnnotationConfigApplicationContext("org.example.prevo.open.adapter");
        this.employmentCommencementProvider = context.getBean(EmploymentCommencementProvider.class);
        this.employmentTerminationProvider = context.getBean(EmploymentTerminationProvider.class);
    }

    @Override
    public EmploymentCommencementProvider getEmploymentCommencementProvider() {
        return employmentCommencementProvider;
    }

    @Override
    public EmploymentTerminationProvider getEmploymentTerminationProvider() {
        return employmentTerminationProvider;
    }

    @Override
    public MatchNotificationListener getMatchNotificationListener() {
        return matchNotificationListener;
    }
}

package ch.prevo.pakt;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.ProviderFactory;

public class PaktProviderFactory implements ProviderFactory {

    private final EmploymentCommencementProvider employmentCommencementProvider;
    private final EmploymentTerminationProvider employmentTerminationProvider;
    private final MatchNotificationListener matchNotificationListener;


    public PaktProviderFactory() {
        final ApplicationContext context = new AnnotationConfigApplicationContext(PaktAdapterConfig.class);
        this.employmentCommencementProvider = context.getBean(EmploymentCommencementProvider.class);
        this.employmentTerminationProvider = context.getBean(EmploymentTerminationProvider.class);
        this.matchNotificationListener = context.getBean(MatchNotificationListener.class);
        
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

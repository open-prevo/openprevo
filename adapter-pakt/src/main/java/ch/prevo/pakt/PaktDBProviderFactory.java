package ch.prevo.pakt;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.ProviderFactory;
import ch.prevo.open.node.data.provider.dummy.DefaultMatchNotificationListener;

public class PaktDBProviderFactory implements ProviderFactory {

    private final EmploymentCommencementProvider employmentStartProvider;
    private final EmploymentTerminationProvider employmentEndProvider;
    private final MatchNotificationListener matchNotificationListener = new DefaultMatchNotificationListener();


    public PaktDBProviderFactory() {
        final ApplicationContext context = new AnnotationConfigApplicationContext(PaktAdapterConfig.class);
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

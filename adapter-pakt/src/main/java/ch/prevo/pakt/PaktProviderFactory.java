package ch.prevo.pakt;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ch.prevo.open.node.data.provider.JobEndProvider;
import ch.prevo.open.node.data.provider.JobStartProvider;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.ProviderFactory;

public class PaktProviderFactory implements ProviderFactory {

    private final JobStartProvider jobStartProvider;
    private final JobEndProvider jobEndProvider;
    private final MatchNotificationListener matchNotificationListener;


    public PaktProviderFactory() {
        final ApplicationContext context = new AnnotationConfigApplicationContext(PaktAdapterConfig.class);
        this.jobStartProvider = context.getBean(JobStartProvider.class);
        this.jobEndProvider = context.getBean(JobEndProvider.class);
        this.matchNotificationListener = context.getBean(MatchNotificationListener.class);
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

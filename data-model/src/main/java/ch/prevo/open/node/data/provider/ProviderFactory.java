package ch.prevo.open.node.data.provider;

public interface ProviderFactory {

    JobStartProvider getJobStartProvider();

    JobEndProvider getJobEndProvider();

}

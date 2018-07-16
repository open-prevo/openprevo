package ch.prevo.open.node.data.provider;

public interface ProviderFactory {

    EmploymentCommencementProvider getEmploymentCommencementProvider();

    EmploymentTerminationProvider getEmploymentTerminationProvider();

    MatchNotificationListener getMatchNotificationListener();
}

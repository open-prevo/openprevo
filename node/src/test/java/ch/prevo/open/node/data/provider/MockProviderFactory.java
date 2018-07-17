package ch.prevo.open.node.data.provider;

public class MockProviderFactory implements ProviderFactory {

    private final MockProvider provider = new MockProvider();

    @Override
    public EmploymentCommencementProvider getEmploymentCommencementProvider() {
        return provider;
    }

    @Override
    public EmploymentTerminationProvider getEmploymentTerminationProvider() {
        return provider;
    }

    @Override
    public MatchNotificationListener getMatchNotificationListener() {
        return provider;
    }
}

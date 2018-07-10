package ch.prevo.open.node.data.provider;

public class MockProviderFactory implements ProviderFactory {

    private final MockProvider provider = new MockProvider();

    @Override
    public JobStartProvider getJobStartProvider() {
        return provider;
    }

    @Override
    public JobEndProvider getJobEndProvider() {
        return provider;
    }

    @Override
    public MatchNotificationListener getMatchNotificationListener() {
        return provider;
    }
}

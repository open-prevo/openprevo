package ch.prevo.open.node.data.provider;

import ch.prevo.open.node.data.provider.dummy.DefaultMatchNotificationListener;


public class JsonProviderFactory implements ProviderFactory {

    private final MatchNotificationListener matchNotificationListener = new DefaultMatchNotificationListener();
    private final JsonAdapter jsonAdapter = new JsonAdapter();

    @Override
    public JobStartProvider getJobStartProvider() {
        return this.jsonAdapter;
    }

    @Override
    public JobEndProvider getJobEndProvider() {
        return this.jsonAdapter;
    }

    @Override
    public MatchNotificationListener getMatchNotificationListener() {
        return this.matchNotificationListener;
    }
}

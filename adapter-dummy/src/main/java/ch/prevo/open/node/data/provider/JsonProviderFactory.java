package ch.prevo.open.node.data.provider;

import ch.prevo.open.node.data.provider.dummy.DefaultMatchNotificationListener;


public class JsonProviderFactory implements ProviderFactory {

    private final MatchNotificationListener matchNotificationListener = new DefaultMatchNotificationListener();
    private final JsonAdapter jsonAdapter = new JsonAdapter();

    @Override
    public EmploymentCommencementProvider getEmploymentCommencementProvider() {
        return this.jsonAdapter;
    }

    @Override
    public EmploymentTerminationProvider getEmploymentTerminationProvider() {
        return this.jsonAdapter;
    }

    @Override
    public MatchNotificationListener getMatchNotificationListener() {
        return this.matchNotificationListener;
    }
}

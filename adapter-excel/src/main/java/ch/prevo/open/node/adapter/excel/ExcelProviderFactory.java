package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.node.data.provider.JobEndProvider;
import ch.prevo.open.node.data.provider.JobStartProvider;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.ProviderFactory;

public class ExcelProviderFactory implements ProviderFactory {

    final ExcelReader excelReader = new ExcelReader();
    final ExcelMatchNotificationListener listener = new ExcelMatchNotificationListener();

    @Override
    public JobStartProvider getJobStartProvider() {
        return excelReader;
    }

    @Override
    public JobEndProvider getJobEndProvider() {
        return excelReader;
    }

    @Override
    public MatchNotificationListener getMatchNotificationListener() {
        return listener;
    }
}

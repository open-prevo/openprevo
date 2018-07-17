package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.ProviderFactory;

public class ExcelProviderFactory implements ProviderFactory {

    final ExcelReader excelReader = new ExcelReader();
    final ExcelMatchNotificationListener listener = new ExcelMatchNotificationListener();

    @Override
    public EmploymentCommencementProvider getEmploymentCommencementProvider() {
        return excelReader;
    }

    @Override
    public EmploymentTerminationProvider getEmploymentTerminationProvider() {
        return excelReader;
    }

    @Override
    public MatchNotificationListener getMatchNotificationListener() {
        return listener;
    }
}

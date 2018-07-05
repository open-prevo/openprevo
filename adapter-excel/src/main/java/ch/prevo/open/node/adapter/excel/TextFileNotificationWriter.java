package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.provider.MatchNotificationProvider;

public class TextFileNotificationWriter implements MatchNotificationProvider {


    //
    // plan:
    // - inject NotificationWriter
    // - obtain some reference to a writeable working directory (possibly where the Excel file resides)
    // - write text files to this directory
    // - extend the MatchNotificationProvider methods to contain the necessary parameters
    // - enjoy a beer
    //


    @Override
    public void notifyEmploymentCommencementMatch() {

    }

    @Override
    public void notifyEmploymentTerminationsMatch() {

    }
}

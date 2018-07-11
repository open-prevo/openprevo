package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ExcelMatchNotificationListener implements MatchNotificationListener {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelMatchNotificationListener.class);

    @Override
    public void handleTerminationMatch(FullTerminationNotification notification) {
        try (final TerminationNotificationWriter writer = new TerminationNotificationWriter()) {
            writer.append(notification);
        } catch (IOException e) {
            LOG.error("Exception while trying to write notification (" + notification +") to Excel-file", e);
        }
    }

    @Override
    public void handleCommencementMatch(FullCommencementNotification notification) {
        try (final CommencementNotificationWriter writer = new CommencementNotificationWriter()) {
            writer.append(notification);
        } catch (IOException e) {
            LOG.error("Exception while trying to write notification (" + notification +") to Excel-file", e);
        }
    }
}

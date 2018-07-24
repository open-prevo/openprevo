package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.FullMatchForCommencementNotification;
import ch.prevo.open.data.api.FullMatchForTerminationNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.error.NotificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ExcelMatchNotificationListener implements MatchNotificationListener {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelMatchNotificationListener.class);


    @Override
    public void handleMatchForCommencementNotification(FullMatchForCommencementNotification notification) throws NotificationException {
        try (final MatchForCommencementNotificationWriter writer = new MatchForCommencementNotificationWriter()) {
            writer.append(notification);
        } catch (IOException e) {
            LOG.error("Exception while trying to write notification (" + notification + ") to Excel-file", e);
            throw new NotificationException(e);
        }
    }

    @Override
    public void handleMatchForTerminationNotification(FullMatchForTerminationNotification notification) throws NotificationException {
        try (final MatchForTerminationNotificationWriter writer = new MatchForTerminationNotificationWriter()) {
            writer.append(notification);
        } catch (IOException e) {
            LOG.error("Exception while trying to write notification (" + notification + ") to Excel-file", e);
            throw new NotificationException(e);
        }
    }

}

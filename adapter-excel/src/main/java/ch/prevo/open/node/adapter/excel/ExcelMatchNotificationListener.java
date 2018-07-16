package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;

public class ExcelMatchNotificationListener implements MatchNotificationListener {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelMatchNotificationListener.class);

    private static final String FILE_PROPERTY = "node.adapter.excel.out.file";
    private static final String FALLBACK_FILE = "retirement-fund-out-data";
    private static final String FILE_NAME_FORMAT = "%1$s_%2$tY-%2$tm-%2$td_%2$tH-%2$tM-%2$tS.%2$tL.xlsx";

    @Override
    public void handleTerminationMatch(FullTerminationNotification notification) {
        final String filename = getFilename();

        try (final TerminationNotificationWriter writer = new TerminationNotificationWriter(filename)) {
            writer.append(notification);
        } catch (IOException e) {
            LOG.error("Exception while trying to write notification (" + notification +") to Excel-file", e);
        }
    }

    @Override
    public void handleCommencementMatch(FullCommencementNotification notification) {
        final String filename = getFilename();

        try (final CommencementNotificationWriter writer = new CommencementNotificationWriter(filename)) {
            writer.append(notification);
        } catch (IOException e) {
            LOG.error("Exception while trying to write notification (" + notification +") to Excel-file", e);
        }
    }

    private static String getFilename() {
        return String.format(FILE_NAME_FORMAT, System.getProperty(FILE_PROPERTY, FALLBACK_FILE), LocalDateTime.now());
    }
}

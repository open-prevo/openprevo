package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.encrypted.model.Address;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.MatchNotification;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NotificationWriterTest {

    @Test
    public void employmentCommencementMatchNotification() throws IOException {
        NotificationWriter notificationWriter = new NotificationWriter();
        StringWriter stringWriter = new StringWriter();
        MatchNotification notification = new MatchNotification("7569678192446", "CHE-109.740.078");
        JobInfo jobInfo =
                new JobInfo("CHE-109.111.222", "our ref 1", "7569678192446", "our internal id", LocalDate.of(2018, 3, 15));

        notificationWriter.write(new PrintWriter(stringWriter), notification, jobInfo);

        String notificationText = stringWriter.toString();
        assertNotNull(notificationText);
        assertEquals(286, notificationText.length());
    }

    @Test
    public void employmentTerminationMatchNotification() throws IOException {
        NotificationWriter notificationWriter = new NotificationWriter();
        StringWriter stringWriter = new StringWriter();
        Address address = new Address("Street", "1234", "Bern");
        CapitalTransferInformation capitalTransferInfo =
                new CapitalTransferInformation("Pension Fund name", "additional name", address, "1-234-567");
        JobInfo jobInfo =
                new JobInfo("CHE-109.111.222", "our ref 1", "7569678192446", "our internal id", LocalDate.of(2018, 3, 15));
        MatchNotification notification = new MatchNotification("7569678192446", "CHE-109.740.078");

        notificationWriter.write(new PrintWriter(stringWriter), notification, capitalTransferInfo, jobInfo);

        String notificationText = stringWriter.toString();
        assertNotNull(notificationText);
        assertEquals(398, notificationText.length());
    }
}
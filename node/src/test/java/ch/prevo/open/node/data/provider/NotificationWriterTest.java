package ch.prevo.open.node.data.provider;

public class NotificationWriterTest {

    // TODO: Fix tests
//    @Test
//    public void employmentCommencementMatchNotification() throws IOException {
//        NotificationWriter notificationWriter = new NotificationWriter();
//        StringWriter stringWriter = new StringWriter();
//        CommencementMatchNotification notification = new CommencementMatchNotification("7569678192446", "CHE-109.740.078");
//        JobInfo jobInfo =
//                new JobInfo("CHE-109.111.222", "our ref 1", "7569678192446", "our internal id", LocalDate.of(2018, 3, 15));
//
//        notificationWriter.write(new PrintWriter(stringWriter), notification, jobInfo);
//
//        String notificationText = stringWriter.toString();
//        assertNotNull(notificationText);
//        assertTrue(notificationText.startsWith("Match found for employment commencement"));
//    }
//
//    @Test
//    public void employmentTerminationMatchNotification() throws IOException {
//        NotificationWriter notificationWriter = new NotificationWriter();
//        StringWriter stringWriter = new StringWriter();
//        Address address = new Address("Street", "1234", "Bern");
//        CapitalTransferInformation capitalTransferInfo =
//                new CapitalTransferInformation("Pension Fund name", "additional name", address, "1-234-567");
//        JobInfo jobInfo =
//                new JobInfo("CHE-109.111.222", "our ref 1", "7569678192446", "our internal id", LocalDate.of(2018, 3, 15));
//        CommencementMatchNotification notification = new CommencementMatchNotification("7569678192446", "CHE-109.740.078");
//
//        notificationWriter.write(new PrintWriter(stringWriter), notification, capitalTransferInfo, jobInfo);
//
//        String notificationText = stringWriter.toString();
//        assertNotNull(notificationText);
//        assertTrue(notificationText.startsWith("Match found for employment termination"));
//    }
}

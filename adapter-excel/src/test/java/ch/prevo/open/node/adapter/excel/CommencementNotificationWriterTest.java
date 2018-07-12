package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.encrypted.model.Address;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static ch.prevo.open.node.adapter.excel.ExcelAssertions.assertRow;

public class CommencementNotificationWriterTest {

    @Test
    public void commencementNotificationShouldGenerateExcelFile() throws IOException, InvalidFormatException {
        // given
        final Address address = new Address();
        address.setStreet("St. Alban-Anlage 26");
        address.setPostalCode("4002");
        address.setCity("Basel");

        final CapitalTransferInformation transferInformation = new CapitalTransferInformation();
        transferInformation.setName("Helvetia Prisma Sammelstiftung");
        transferInformation.setAdditionalName("Helvetia Versicherungen Schweiz");
        transferInformation.setAddress(address);
        transferInformation.setIban("CH52 0483 5012 3456 7100 0");
        transferInformation.setReferenceId("756.1335.5778.23");

        final JobInfo jobInfo = new JobInfo();
        jobInfo.setRetirementFundUid("CHE-109.740.084-Baloise-Sammelstiftung");
        jobInfo.setOasiNumber("756.1335.5778.23");
        jobInfo.setDate(LocalDate.of(2018, 6, 30));
        jobInfo.setInternalReferenz("baloise-1");

        final JobEnd jobEnd = new JobEnd();
        jobEnd.setJobInfo(jobInfo);

        final FullCommencementNotification notification = new FullCommencementNotification();
        notification.setNewRetirementFundUid("CHE-109.537.488-Helvetia-Prisma-Sammelstiftung");
        notification.setCommencementDate(LocalDate.of(2018, 7, 1));
        notification.setTransferInformation(transferInformation);
        notification.setJobEnd(jobEnd);

        final String filename = File.createTempFile("openprevo_text", ".xlsx").getAbsolutePath();

        // when
        final CommencementNotificationWriter writer = new CommencementNotificationWriter(filename);
        writer.append(notification);
        writer.close();

        // then
        assertRow(filename, "Austritte", 0,
                "AHV-Nummer",
                "Austritt",
                "UID der eigenen VE",
                "Eigene Referenz",
                "Eintritt",
                "UID der neuen VE",
                "Name der neuen VE",
                "Zusatzname",
                "Strasse / Postfach",
                "PLZ",
                "Ort",
                "IBAN",
                "Referenznr. der neuen VE"
        );

        assertRow(filename, "Austritte", 1,
                jobInfo.getOasiNumber(),
                jobInfo.getDate(),
                jobInfo.getRetirementFundUid(),
                jobInfo.getInternalReferenz(),
                notification.getCommencementDate(),
                notification.getNewRetirementFundUid(),
                transferInformation.getName(),
                transferInformation.getAdditionalName(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCity(),
                transferInformation.getIban(),
                transferInformation.getReferenceId()
        );
    }

}

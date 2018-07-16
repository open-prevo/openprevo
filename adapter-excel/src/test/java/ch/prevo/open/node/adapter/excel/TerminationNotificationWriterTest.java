package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.encrypted.model.Address;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static ch.prevo.open.node.adapter.excel.ExcelAssertions.assertRow;

public class TerminationNotificationWriterTest {

    @Test
    public void terminationNotificationShouldGenerateExcelFile() throws IOException, InvalidFormatException {
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
        jobInfo.setRetirementFundUid("CHE-109.537.488-Helvetia-Prisma-Sammelstiftung");
        jobInfo.setOasiNumber("756.1335.5778.23");
        jobInfo.setDate(LocalDate.of(2018, 7, 1));
        jobInfo.setInternalReferenz("helvetia-1");

        final EmploymentCommencement jobStart = new EmploymentCommencement();
        jobStart.setCapitalTransferInfo(transferInformation);
        jobStart.setJobInfo(jobInfo);

        final FullTerminationNotification notification = new FullTerminationNotification();
        notification.setPreviousRetirementFundUid("CHE-109.740.084-Baloise-Sammelstiftung");
        notification.setTerminationDate(LocalDate.of(2018, 6, 30));
        notification.setEmploymentCommencement(jobStart);

        final String filename = File.createTempFile("openprevo_text", ".xlsx").getAbsolutePath();

        // when
        final TerminationNotificationWriter writer = new TerminationNotificationWriter(filename);
        writer.append(notification);
        writer.close();

        // then
        assertRow(filename, "Eintritte", 0,
                "AHV-Nummer",
                "Eintritt",
                "UID der eigenen VE",
                "Eigene Referenz",
                "Austritt",
                "UID der ehemaligen VE"
        );

        assertRow(filename, "Eintritte", 1,
                jobInfo.getOasiNumber(),
                jobInfo.getDate(),
                jobInfo.getRetirementFundUid(),
                jobInfo.getInternalReferenz(),
                notification.getTerminationDate(),
                notification.getPreviousRetirementFundUid()
        );
    }

}

package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.*;
import ch.prevo.open.encrypted.model.Address;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExcelReaderTest {

    @Test
    public void readEmploymentTerminations() throws Exception {
        List<JobEnd> jobEnds = getExcelReader().getJobEnds();
        assertEquals(2, jobEnds.size());

        JobInfo jobInfo = jobEnds.get(0).getJobInfo();
        assertEquals("7561453383445", jobInfo.getOasiNumber());
        assertEquals(LocalDate.of(2018, 6, 1), jobInfo.getDate());
        assertEquals("CHE-109.740.078", jobInfo.getRetirementFundUid());

    }

    @Test
    public void readEmploymentStarts() throws Exception {
        List<JobStart> jobStarts = getExcelReader().getJobStarts();

        assertEquals(2, jobStarts.size());

        JobStart jobStart = jobStarts.get(1);

        JobInfo jobInfo = jobStart.getJobInfo();
        assertEquals("7569678192446", jobInfo.getOasiNumber());
        assertEquals(LocalDate.of(2018, 8, 15), jobInfo.getDate());
        assertEquals("CHE-109.740.078", jobInfo.getRetirementFundUid());
        assertEquals("our-ref-56", jobInfo.getInternalReferenz());

        CapitalTransferInformation capitalTransferInfo = jobStart.getCapitalTransferInfo();
        assertEquals("Zusatzname", capitalTransferInfo.getAdditionalName());
        assertEquals("IBAN", capitalTransferInfo.getIban());

        Address address = capitalTransferInfo.getAddress();
        assertEquals("Str/Postfach", address.getStreet());
        assertEquals("PLZ", address.getPostalCode());
        assertEquals("Ort", address.getCity());
    }

    private ExcelReader getExcelReader() throws Exception {
        return new ExcelReader("/retirement-fund-test-data_de.xlsx");
    }

}
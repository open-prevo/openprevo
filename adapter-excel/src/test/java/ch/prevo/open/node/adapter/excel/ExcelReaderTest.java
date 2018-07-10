package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.data.api.JobStart;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExcelReaderTest {

	@Before
	public void init(){
		System.setProperty(ExcelReader.FILE_PROPERTY, "src/test/resources/retirement-fund-test-data_de.xlsx");
	}

    @Test
    public void readEmploymentTerminations() throws Exception {
        List<JobEnd> employmentTerminations = getExcelReader().getJobEnds();
        assertEquals(2, employmentTerminations.size());

        JobInfo jobInfo = employmentTerminations.get(1).getJobInfo();
        assertEquals("7568152139908", jobInfo.getOasiNumber());
        assertEquals(LocalDate.of(2018, 8, 16), jobInfo.getDate());
        assertEquals("CHE-223.471.073", jobInfo.getRetirementFundUid());
    }

    @Test
    public void readEmploymentCommencements() throws Exception {
        List<JobStart> employmentCommencements = getExcelReader().getJobStarts();

        assertEquals(2, employmentCommencements.size());


        JobInfo jobInfo = employmentCommencements.get(0).getJobInfo();
        assertEquals("7566374437536", jobInfo.getOasiNumber());
        assertEquals(LocalDate.of(2018, 1, 1), jobInfo.getDate());
        assertEquals("CHE-109.740.084", jobInfo.getRetirementFundUid());
    }

    private ExcelReader getExcelReader() throws Exception {
        return new ExcelReader();
    }
}
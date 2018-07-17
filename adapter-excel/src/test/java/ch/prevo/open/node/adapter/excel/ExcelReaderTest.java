package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.EmploymentCommencement;
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
        List<EmploymentTermination> employmentTerminations = getExcelReader().getEmploymentTerminations();
        assertEquals(2, employmentTerminations.size());

        EmploymentInfo employmentInfo = employmentTerminations.get(1).getEmploymentInfo();
        assertEquals("7568152139908", employmentInfo.getOasiNumber());
        assertEquals(LocalDate.of(2018, 8, 16), employmentInfo.getDate());
        assertEquals("CHE-223.471.073", employmentInfo.getRetirementFundUid());
    }

    @Test
    public void readEmploymentCommencements() throws Exception {
        List<EmploymentCommencement> employmentCommencements = getExcelReader().getEmploymentCommencements();

        assertEquals(2, employmentCommencements.size());


        EmploymentInfo employmentInfo = employmentCommencements.get(0).getEmploymentInfo();
        assertEquals("7566374437536", employmentInfo.getOasiNumber());
        assertEquals(LocalDate.of(2018, 1, 1), employmentInfo.getDate());
        assertEquals("CHE-109.740.084", employmentInfo.getRetirementFundUid());
    }

    private ExcelReader getExcelReader() throws Exception {
        return new ExcelReader();
    }
}

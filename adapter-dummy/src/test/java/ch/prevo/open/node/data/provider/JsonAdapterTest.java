package ch.prevo.open.node.data.provider;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.data.api.EmploymentCommencement;

public class JsonAdapterTest {

    private JsonAdapter jsonAdapter;

    @Before
    public void setUp() {
        this.jsonAdapter = new JsonAdapter("classpath:employment-commencement-test.json", "classpath:employment-termination-test.json");
    }

    @Test
    public void getEmploymentCommencementInformation() {
        List<EmploymentCommencement> employmentEndInformation = jsonAdapter.getEmploymentCommencements();
        assertThat(employmentEndInformation).hasSize(3);
    }

    @Test
    public void getEmploymentTerminationInformation() {
        List<EmploymentTermination> employmentEndInformation = jsonAdapter.getEmploymentTerminations();
        assertThat(employmentEndInformation).hasSize(3);
    }
}

package ch.prevo.open.node.data.provider;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobStart;

public class JsonAdapterTest {

    private JsonAdapter jsonAdapter;

    @Before
    public void setUp() {
        this.jsonAdapter = new JsonAdapter("classpath:employment-commencement-test.json", "classpath:employment-termination-test.json");
    }

    @Test
    public void getJobStartInformation() {
        List<JobStart> jobEndInformation = jsonAdapter.getJobStarts();
        assertThat(jobEndInformation).hasSize(3);
    }

    @Test
    public void getJobEndInformation() {
        List<JobEnd> jobEndInformation = jsonAdapter.getJobEnds();
        assertThat(jobEndInformation).hasSize(3);
    }
}
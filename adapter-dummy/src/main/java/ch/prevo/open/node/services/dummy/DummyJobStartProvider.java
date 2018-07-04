package ch.prevo.open.node.services.dummy;

import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.data.api.JobStart;
import ch.prevo.open.node.services.JobStartProvider;

import java.util.Arrays;
import java.util.List;

public class DummyJobStartProvider implements JobStartProvider {

    @Override
    public List<JobStart> getJobStarts() {
        return Arrays.asList(
                new JobStart(null, new JobInfo("CHE-109.740.084", null, "756.1234.5678.97", null), null),
                new JobStart(null, new JobInfo("CHE-109.740.078", null, "756.5678.1234.17", null), null),
                new JobStart(null, new JobInfo("CHE-109.537.488", null, "756.1298.6578.97", null), null)
        );
    }

}

package ch.prevo.open.node.data.provider.dummy;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.data.api.JobStart;
import ch.prevo.open.node.data.provider.JobEndProvider;
import ch.prevo.open.node.data.provider.JobStartProvider;

import java.util.Arrays;
import java.util.List;

public class DummyProvider implements JobStartProvider, JobEndProvider {

    @Override
    public List<JobStart> getJobStarts() {
        return Arrays.asList(
                new JobStart(null, new JobInfo("CHE-109.740.084", null, "756.1234.5678.97", null, null), null),
                new JobStart(null, new JobInfo("CHE-109.740.078", null, "756.5678.1234.17", null, null), null),
                new JobStart(null, new JobInfo("CHE-109.537.488", null, "756.1298.6578.97", null, null), null)
        );
    }

    @Override
    public List<JobEnd> getJobEnds() {
        return Arrays.asList(
                new JobEnd(null, new JobInfo("CHE-109.537.488", null, "756.3412.8844.97", null, null)),
                new JobEnd(null, new JobInfo("CHE-109.740.084", null, "756.1335.5778.23", null, null)),
                new JobEnd(null, new JobInfo("CHE-109.740.078", null, "756.9534.5271.94", null, null))
        );
    }

}

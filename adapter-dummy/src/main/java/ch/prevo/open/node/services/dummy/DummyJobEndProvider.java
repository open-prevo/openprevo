package ch.prevo.open.node.services.dummy;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.node.services.JobEndProvider;

import java.util.Arrays;
import java.util.List;

public class DummyJobEndProvider implements JobEndProvider {

    @Override
    public List<JobEnd> getJobEnds() {
        return Arrays.asList(
                new JobEnd(null, new JobInfo("CHE-109.537.488", null, "756.3412.8844.97", null)),
                new JobEnd(null, new JobInfo("CHE-109.740.084", null, "756.1335.5778.23", null)),
                new JobEnd(null, new JobInfo("CHE-109.740.078", null, "756.9534.5271.94", null))
        );
    }

}

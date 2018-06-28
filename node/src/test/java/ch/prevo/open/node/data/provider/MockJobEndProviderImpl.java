package ch.prevo.open.node.data.provider;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.data.api.JobStart;

//@Service
public class MockJobEndProviderImpl implements JobEndProvider, JobStartProvider {
	
	@Override
	public List<JobEnd> getJobEnds() {
		return Arrays.asList(
				new JobEnd("1",
						new JobInfo().setOasiNumber("756.3412.8844.97").setRetirementFundUid("CHE-109.537.488")),
				new JobEnd("2",
						new JobInfo().setOasiNumber("756.1335.5778.23").setRetirementFundUid("CHE-109.740.084")),
				new JobEnd("3",
						new JobInfo().setOasiNumber("756.9534.5271.94").setRetirementFundUid("CHE-109.740.078")));
	}

	@Override
	public List<JobStart> getJobStarts() {
		return Arrays.asList(
				new JobStart("1",
						new JobInfo().setOasiNumber("756.3412.8844.97").setRetirementFundUid("CHE-109.537.488")),
				new JobStart("2",
						new JobInfo().setOasiNumber("756.1335.5778.23").setRetirementFundUid("CHE-109.740.084")),
				new JobStart("3",
						new JobInfo().setOasiNumber("756.9534.5271.94").setRetirementFundUid("CHE-109.740.078")));
	}
}

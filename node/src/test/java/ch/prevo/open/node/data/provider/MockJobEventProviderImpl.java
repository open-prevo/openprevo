package ch.prevo.open.node.data.provider;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.data.api.JobStart;

@Service
public class MockJobEventProviderImpl implements JobEndProvider, JobStartProvider {
	
	@Override
	public List<JobEnd> getJobEnds() {
		int i = 0;
		return Arrays.asList(
				new JobEnd(Integer.toString(++i),
						new JobInfo().setOasiNumber("756.3412.8844.97").setRetirementFundUid("CHE-109.537.488")),
				new JobEnd(Integer.toString(++i),
						new JobInfo().setOasiNumber("756.1335.5778.23").setRetirementFundUid("CHE-109.740.084")),
				new JobEnd(Integer.toString(++i),
						new JobInfo().setOasiNumber("756.9534.5271.94").setRetirementFundUid("CHE-109.740.078")));
	}

	@Override
	public List<JobStart> getJobStarts() {
		int i = 0;		
		return Arrays.asList(
				new JobStart(Integer.toString(++i),
						new JobInfo().setOasiNumber("756.1234.5678.97").setRetirementFundUid("CHE-109.740.084")),
				new JobStart(Integer.toString(++i),
						new JobInfo().setOasiNumber("756.5678.1234.17").setRetirementFundUid("CHE-109.740.078")),
				new JobStart(Integer.toString(++i),
						new JobInfo().setOasiNumber("756.1298.6578.97").setRetirementFundUid("CHE-109.537.488")));
	}
}

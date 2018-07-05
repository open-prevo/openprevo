package ch.prevo.open.node.data.provider;

import ch.prevo.open.data.api.CapitalTransferInformation;
import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.data.api.JobStart;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MockJobEventProviderImpl implements JobEndProvider, JobStartProvider {
	
	@Override
	public List<JobEnd> getJobEnds() {
		int i = 0;
		return Arrays.asList(
				new JobEnd(Integer.toString(++i), new JobInfo("CHE-109.537.488", "756.3412.8844.97")),
				new JobEnd(Integer.toString(++i), new JobInfo("CHE-109.740.084", "756.1335.5778.23")),
				new JobEnd(Integer.toString(++i), new JobInfo("CHE-109.740.078", "756.9534.5271.94")));
	}

	@Override
	public List<JobStart> getJobStarts() {
		int i = 0;		
		return Arrays.asList(
				new JobStart(Integer.toString(++i), new JobInfo("CHE-109.740.084", "756.1234.5678.97"), new CapitalTransferInformation("dummyName", "dummyIban")),
				new JobStart(Integer.toString(++i), new JobInfo("CHE-109.740.078", "756.5678.1234.17"), new CapitalTransferInformation("dummyName", "dummyIban")),
				new JobStart(Integer.toString(++i), new JobInfo("CHE-109.537.488", "756.1298.6578.97"), new CapitalTransferInformation("dummyName", "dummyIban")));
	}
}

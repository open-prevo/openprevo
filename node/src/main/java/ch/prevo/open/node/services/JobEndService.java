package ch.prevo.open.node.services;

import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;

import org.springframework.stereotype.Service;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.node.data.provider.JobEndProvider;

/**
 * Service implementation to retrieve encrypted information about an insurant.
 */
@Service
public class JobEndService {

	@Inject
	private JobEndProvider jobEndProvider;

	public Set<InsurantInformation> getAllJobEndData() {
		return jobEndProvider.getJobEnds().stream()
				.map(jobEnd -> new InsurantInformation(jobEnd.getJobInfo().getOasiNumber(),
						jobEnd.getJobInfo().getRetirementFundUid(), jobEnd.getJobInfo().getDate()))
				.collect(Collectors.toSet());
	}
}

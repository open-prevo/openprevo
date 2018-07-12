package ch.prevo.open.node.services;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.node.config.AdapterServiceConfiguration;
import ch.prevo.open.node.data.provider.JobStartProvider;
import ch.prevo.open.node.data.provider.ProviderFactory;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation to retrieve encrypted information about an insurant.
 */
@Service
public class JobStartService {

    private final JobStartProvider jobStartProvider;
    private final Cryptography cryptography;

    @Inject
    public JobStartService(ServiceListFactoryBean factoryBean, Cryptography cryptography) {
        final ProviderFactory factory = AdapterServiceConfiguration.getAdapterService(factoryBean);
        jobStartProvider = factory != null? factory.getJobStartProvider() : null;
        this.cryptography = cryptography;
    }

	public Set<InsurantInformation> getAllJobStartData() {
		return jobStartProvider.getJobStarts().stream()
				.map(jobEnd -> new InsurantInformation(cryptography.hash(jobEnd.getJobInfo().getOasiNumber()),
						jobEnd.getJobInfo().getRetirementFundUid(), jobEnd.getJobInfo().getDate()))
				.collect(Collectors.toSet());
	}
}

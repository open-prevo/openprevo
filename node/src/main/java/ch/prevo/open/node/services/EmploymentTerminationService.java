package ch.prevo.open.node.services;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.services.Cryptography;
import ch.prevo.open.node.config.AdapterServiceConfiguration;
import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
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
public class EmploymentTerminationService {

    private final EmploymentTerminationProvider jobEndProvider;

    @Inject
    public EmploymentTerminationService(ServiceListFactoryBean factoryBean) {
        final ProviderFactory factory = AdapterServiceConfiguration.getAdapterService(factoryBean);
        this.jobEndProvider = factory != null? factory.getEmploymentTerminationProvider() : null;
    }

	public Set<InsurantInformation> getAllEmploymentTerminationData() {
		return jobEndProvider.getEmploymentTerminations().stream()
				.map(jobEnd -> new InsurantInformation(Cryptography.digestOasiNumber(jobEnd.getJobInfo().getOasiNumber()),
						jobEnd.getJobInfo().getRetirementFundUid(), jobEnd.getJobInfo().getDate()))
				.collect(Collectors.toSet());
	}
}

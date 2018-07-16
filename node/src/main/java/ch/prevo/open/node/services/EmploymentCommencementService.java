package ch.prevo.open.node.services;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.services.Cryptography;
import ch.prevo.open.node.config.AdapterServiceConfiguration;
import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
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
public class EmploymentCommencementService {

    private final EmploymentCommencementProvider employmentStartProvider;

    @Inject
    public EmploymentCommencementService(ServiceListFactoryBean factoryBean) {
        final ProviderFactory factory = AdapterServiceConfiguration.getAdapterService(factoryBean);
        employmentStartProvider = factory != null? factory.getEmploymentCommencementProvider() : null;
    }

	public Set<InsurantInformation> getAllEmploymentCommencementData() {
		return employmentStartProvider.getEmploymentCommencements().stream()
				.map(employmentEnd -> new InsurantInformation(Cryptography.digestOasiNumber(employmentEnd.getEmploymentInfo().getOasiNumber()),
						employmentEnd.getEmploymentInfo().getRetirementFundUid(), employmentEnd.getEmploymentInfo().getDate()))
				.collect(Collectors.toSet());
	}
}

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

    private final EmploymentCommencementProvider employmentCommencementProvider;

    @Inject
    public EmploymentCommencementService(ServiceListFactoryBean factoryBean) {
        final ProviderFactory factory = AdapterServiceConfiguration.getAdapterService(factoryBean);
        employmentCommencementProvider = factory != null? factory.getEmploymentCommencementProvider() : null;
    }

	public Set<InsurantInformation> getAllEmploymentCommencementData() {
		return employmentCommencementProvider.getEmploymentCommencements().stream()
				.map(employmentTermination -> new InsurantInformation(Cryptography.digestOasiNumber(employmentTermination.getEmploymentInfo().getOasiNumber()),
						employmentTermination.getEmploymentInfo().getRetirementFundUid(), employmentTermination.getEmploymentInfo().getDate()))
				.collect(Collectors.toSet());
	}
}

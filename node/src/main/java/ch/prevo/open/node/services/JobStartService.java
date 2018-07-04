package ch.prevo.open.node.services;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.node.config.AdapterServiceConfiguration;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Dummy mock service implementation to retrieve encrypted information about an insurant.
 */
@Service
public class JobStartService {

    private final JobStartProvider jobStartProvider;

    @Inject
    public JobStartService(@Named("jobStartProviderFactory") ServiceListFactoryBean factory) {
        jobStartProvider = AdapterServiceConfiguration.getAdapterService(factory);
    }

    public Set<InsurantInformation> getAllJobStartData() {
        return jobStartProvider.getJobStarts().stream()
                .map(jobEnd -> new InsurantInformation(jobEnd.getJobInfo().getOasiNumber(),
                        jobEnd.getJobInfo().getRetirementFundUid()))
                .collect(Collectors.toSet());
    }
}

package ch.prevo.open.node.services;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.node.config.AdapterServiceConfiguration;
import ch.prevo.open.node.data.provider.JobEndProvider;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation to retrieve encrypted information about an insurant.
 */
@Service
public class JobEndService {

    private final JobEndProvider jobEndProvider;

    @Inject
    public JobEndService(@Named("jobEndServiceFactory") ServiceListFactoryBean factory) {
        jobEndProvider = AdapterServiceConfiguration.getAdapterService(factory);
    }

    public Set<InsurantInformation> getAllJobEndData() {
        return jobEndProvider.getJobEnds().stream()
                .map(jobEnd -> new InsurantInformation(jobEnd.getJobInfo().getOasiNumber(),
                        jobEnd.getJobInfo().getRetirementFundUid()))
                .collect(Collectors.toSet());
    }
}

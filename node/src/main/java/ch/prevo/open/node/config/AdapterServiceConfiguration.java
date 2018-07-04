package ch.prevo.open.node.config;

import ch.prevo.open.node.services.JobEndProvider;
import ch.prevo.open.node.services.JobStartProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AdapterServiceConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterServiceConfiguration.class);

    @Bean
    public ServiceListFactoryBean jobStartProviderFactory() {
        final ServiceListFactoryBean serviceListFactoryBean = new ServiceListFactoryBean();
        serviceListFactoryBean.setServiceType(JobStartProvider.class);
        return serviceListFactoryBean;
    }

    @Bean
    public ServiceListFactoryBean jobEndServiceFactory() {
        ServiceListFactoryBean serviceListFactoryBean = new ServiceListFactoryBean();
        serviceListFactoryBean.setServiceType(JobEndProvider.class);
        return serviceListFactoryBean;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAdapterService(ServiceListFactoryBean factory) {
        try {
            final List<T> providers = (List<T>) factory.getObject();
            if (providers.size() == 1) {
                return providers.get(0);
            } else if (providers.isEmpty()) {
                LOG.error("No adapter found");
            } else {
                LOG.error("More than one adapter found");
            }
        } catch (Exception e) {
            LOG.error("Setup of adapter-service failed", e);
        }
        return null;

    }


}

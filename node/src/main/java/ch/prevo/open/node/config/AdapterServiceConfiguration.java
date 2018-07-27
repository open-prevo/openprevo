/*============================================================================*
 * Copyright (c) 2018 - Prevo-System AG and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 3
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-3.0 WITH Classpath-exception-2.0
 * 
 * Contributors:
 *     Prevo-System AG - initial API and implementation
 *===========================================================================*/
package ch.prevo.open.node.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.prevo.open.node.data.provider.ProviderFactory;

@Configuration
public class AdapterServiceConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterServiceConfiguration.class);

    @Bean
    public ServiceListFactoryBean factory() {
        final ServiceListFactoryBean serviceListFactoryBean = new ServiceListFactoryBean();
        serviceListFactoryBean.setSingleton(true);
        serviceListFactoryBean.setServiceType(ProviderFactory.class);
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
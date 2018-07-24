/*******************************************************************************
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
 ******************************************************************************/
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

    private final AdapterDataValidationService adapterDataValidationService;

    @Inject
    public EmploymentCommencementService(ServiceListFactoryBean factoryBean, AdapterDataValidationService adapterDataValidationService) {
        this.adapterDataValidationService = adapterDataValidationService;
        final ProviderFactory factory = AdapterServiceConfiguration.getAdapterService(factoryBean);
        employmentCommencementProvider = factory != null? factory.getEmploymentCommencementProvider() : null;
    }

	public Set<InsurantInformation> getAllEmploymentCommencementData() {
		return employmentCommencementProvider.getEmploymentCommencements().stream()
                .filter(adapterDataValidationService::isValidEmploymentCommencement)
				.map(employmentTermination -> new InsurantInformation(Cryptography.digestOasiNumber(employmentTermination.getEmploymentInfo().getOasiNumber()),
						employmentTermination.getEmploymentInfo().getRetirementFundUid(), employmentTermination.getEmploymentInfo().getDate()))
				.collect(Collectors.toSet());
	}
}

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

import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.services.Cryptography;
import ch.prevo.open.node.config.AdapterServiceConfiguration;
import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import ch.prevo.open.node.data.provider.ProviderFactory;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation to retrieve encrypted information about an insurant.
 */
@Service
public class EmploymentTerminationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmploymentTerminationService.class);

    private final EmploymentTerminationProvider employmentTerminationProvider;

    private final AdapterDataValidationService adapterDataValidationService;

    @Inject
    public EmploymentTerminationService(ServiceListFactoryBean factoryBean, AdapterDataValidationService adapterDataValidationService) {
        this.adapterDataValidationService = adapterDataValidationService;
        final ProviderFactory factory = AdapterServiceConfiguration.getAdapterService(factoryBean);
        this.employmentTerminationProvider = factory != null? factory.getEmploymentTerminationProvider() : null;
    }

	public Set<InsurantInformation> getAllEmploymentTerminationData() {
        final List<EmploymentTermination> terminations = employmentTerminationProvider.getEmploymentTerminations();

        final Map<Boolean, Map<EmploymentTermination, Set<ConstraintViolation<EmploymentTermination>>>> validations = terminations.stream()
                .map(termination -> Pair.of(termination, adapterDataValidationService.getEmploymentTerminationViolations(termination)))
                .collect(Collectors.groupingBy(pair -> pair.getRight().isEmpty(), Collectors.toMap(Pair::getLeft, Pair::getRight)));

        final Map<EmploymentTermination, Set<ConstraintViolation<EmploymentTermination>>> violations = validations.get(false);
        if (violations != null) {
            LOGGER.warn("Invalid commencement found in provided data, error: \n{}", violations);
            employmentTerminationProvider.notifyTerminationErrors(violations);
        }

        return validations.get(true).keySet().stream()
                .map(termination ->
                        new InsurantInformation(
                                Cryptography.digestOasiNumber(termination.getEmploymentInfo().getOasiNumber()),
                                termination.getEmploymentInfo().getRetirementFundUid(),
                                termination.getEmploymentInfo().getDate()
                        )
                )
                .collect(Collectors.toSet());
	}
}

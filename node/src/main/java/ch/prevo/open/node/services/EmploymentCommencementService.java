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

import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.services.Cryptography;
import ch.prevo.open.node.config.AdapterServiceConfiguration;
import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
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
public class EmploymentCommencementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmploymentCommencementService.class);

    private final EmploymentCommencementProvider employmentCommencementProvider;

    private final AdapterDataValidationService adapterDataValidationService;

    @Inject
    public EmploymentCommencementService(ServiceListFactoryBean factoryBean, AdapterDataValidationService adapterDataValidationService) {
        this.adapterDataValidationService = adapterDataValidationService;
        final ProviderFactory factory = AdapterServiceConfiguration.getAdapterService(factoryBean);
        employmentCommencementProvider = factory != null ? factory.getEmploymentCommencementProvider() : null;
    }

    public Set<InsurantInformation> getAllEmploymentCommencementData() {
        final List<EmploymentCommencement> commencements = employmentCommencementProvider.getEmploymentCommencements();

        final Map<Boolean, Map<EmploymentCommencement, Set<ConstraintViolation<EmploymentCommencement>>>> validations = commencements.stream()
                .map(commencement -> Pair.of(commencement, adapterDataValidationService.getEmploymentCommencementViolations(commencement)))
                .collect(Collectors.groupingBy(pair -> pair.getRight().isEmpty(), Collectors.toMap(Pair::getLeft, Pair::getRight)));

        final Map<EmploymentCommencement, Set<ConstraintViolation<EmploymentCommencement>>> violations = validations.get(false);
        if (violations != null) {
            LOGGER.warn("Invalid commencement found in provided data, error: \n{}", violations);
            employmentCommencementProvider.notifyCommencementErrors(violations);
        }

        return validations.get(true).keySet().stream()
                .map(commencement ->
                        new InsurantInformation(
                                Cryptography.digestOasiNumber(commencement.getEmploymentInfo().getOasiNumber()),
                                commencement.getEmploymentInfo().getRetirementFundUid(),
                                commencement.getEmploymentInfo().getDate()
                        )
                )
                .collect(Collectors.toSet());
    }
}

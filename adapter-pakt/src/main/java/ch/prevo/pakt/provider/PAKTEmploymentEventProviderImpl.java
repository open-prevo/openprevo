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
package ch.prevo.pakt.provider;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.encrypted.model.Address;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import ch.prevo.pakt.config.PaktEnvironment;
import ch.prevo.pakt.config.RetirementFundRegistry;
import ch.prevo.pakt.entities.TozsPtverm;
import ch.prevo.pakt.repository.PartnerVermittlungRepository;
import ch.prevo.pakt.zd.utils.CdMeld;

@Service
public class PAKTEmploymentEventProviderImpl implements EmploymentTerminationProvider, EmploymentCommencementProvider {

    private final PartnerVermittlungRepository repository;

    private final PaktEnvironment config;

    private final RetirementFundRegistry retirementFundRegistry;

    @Inject
    public PAKTEmploymentEventProviderImpl(PartnerVermittlungRepository repository,
                                           PaktEnvironment config,
                                           RetirementFundRegistry retirementFundRegistry) {
        this.repository = repository;
        this.config = config;
        this.retirementFundRegistry = retirementFundRegistry;
    }

    @Override
    public List<EmploymentTermination> getEmploymentTerminations() {
        return repository.findByIdCdmandantAndCdmeld(getCdMandant(), CdMeld.DADURCHF.getCode()).stream()
                .map(ptVerm -> buildEmploymentTermination(ptVerm)).collect(Collectors.toList());
    }

    private EmploymentTermination buildEmploymentTermination(TozsPtverm ptVerm) {
        return new EmploymentTermination(Integer.toString(ptVerm.getId().getId()), buildEmploymentInfo(ptVerm));
    }

    private EmploymentInfo buildEmploymentInfo(TozsPtverm ptVerm) {
        EmploymentInfo employmentInfo = new EmploymentInfo();
        employmentInfo.setOasiNumber(ptVerm.getAhv());
        employmentInfo.setRetirementFundUid(getRetirementFundUid(ptVerm));
        employmentInfo.setInternalPersonId(ptVerm.getIdgeschaeftpol());
        employmentInfo.setInternalReferenz(ptVerm.getNameve());
        employmentInfo.setDate(ptVerm.getDtgueltab());
        return employmentInfo;
    }

    private String getRetirementFundUid(TozsPtverm ptVerm) {
        return retirementFundRegistry.getByCdStf(ptVerm.getCdstf()).getUid();
    }

    @Override
    public List<EmploymentCommencement> getEmploymentCommencements() {
        return repository.findByIdCdmandantAndCdmeld(getCdMandant(), CdMeld.NEUEINTRERF.getCode()).stream()
                .map(ptVerm -> buildEmploymentCommencement(ptVerm)).collect(Collectors.toList());
    }

    private EmploymentCommencement buildEmploymentCommencement(TozsPtverm ptVerm) {
        return new EmploymentCommencement(Integer.toString(ptVerm.getId().getId()), buildEmploymentInfo(ptVerm),
                buildCapitalTransferInformation(ptVerm));
    }

    private CapitalTransferInformation buildCapitalTransferInformation(TozsPtverm ptVerm) {
        CapitalTransferInformation capitalTransferInfo = new CapitalTransferInformation(ptVerm.getNameve(),
                ptVerm.getTxtiban());
        capitalTransferInfo.setReferenceId(ptVerm.getIdgeschaeftpol());
        capitalTransferInfo.setAddress(buildAddress(ptVerm));
        capitalTransferInfo.setAdditionalName(ptVerm.getNamezusatz());
        return capitalTransferInfo;
    }

    private Address buildAddress(TozsPtverm ptVerm) {
        Address address = new Address();
        address.setCity(ptVerm.getTxtort());
        address.setPostalCode(ptVerm.getTxtpostleitzahl());
        address.setStreet(ptVerm.getNamestrasse());
        return address;
    }

    private Short getCdMandant() {
        return config.getCdMandant();
    }
}
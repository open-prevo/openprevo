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
package ch.prevo.open.node.services;

import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.stereotype.Service;

import ch.prevo.open.data.api.CapitalTransferInformation;
import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.data.api.FullMatchForCommencementNotification;
import ch.prevo.open.data.api.FullMatchForTerminationNotification;
import ch.prevo.open.encrypted.model.EncryptedData;
import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.encrypted.model.MatchForTermination;
import ch.prevo.open.encrypted.services.Cryptography;
import ch.prevo.open.node.config.AdapterServiceConfiguration;
import ch.prevo.open.node.config.NodeConfigurationService;
import ch.prevo.open.node.crypto.CapitalTransferInfoEncrypter;
import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.ProviderFactory;
import ch.prevo.open.node.data.provider.error.NotificationException;

@Service
public class MatchNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(MatchNotificationService.class);

    private final MatchNotificationListener listener;

    private final EmploymentCommencementProvider employmentCommencementProvider;
    private final EmploymentTerminationProvider employmentTerminationProvider;
    private final NodeConfigurationService nodeConfigService;

    @Inject
    public MatchNotificationService(ServiceListFactoryBean factoryBean, NodeConfigurationService nodeConfigService) {
        final ProviderFactory factory = AdapterServiceConfiguration.getAdapterService(factoryBean);
        this.employmentCommencementProvider = factory != null ? factory.getEmploymentCommencementProvider() : null;
        this.employmentTerminationProvider = factory != null ? factory.getEmploymentTerminationProvider() : null;
        this.listener = factory != null ? factory.getMatchNotificationListener() : null;
        this.nodeConfigService = nodeConfigService;
    }

    public void handleCommencementMatch(MatchForTermination notification) throws NotificationException {
        final Optional<EmploymentTermination> employmentTermination = employmentTerminationProvider.getEmploymentTerminations().stream()
                .filter(currentEmploymentTermination -> isSameAsNotification(currentEmploymentTermination, notification))
                .findAny();

        if (!employmentTermination.isPresent()) {
            LOG.warn("Termination notification received which does not correlate with any employment start: {}", notification);
            return;
        }

        final FullMatchForTerminationNotification fullNotification = new FullMatchForTerminationNotification();
        fullNotification.setNewRetirementFundUid(notification.getNewRetirementFundUid());
        fullNotification.setCommencementDate(notification.getCommencementDate());
        fullNotification.setEmploymentTermination(employmentTermination.get());

        EncryptedData encryptedCapitalTransferInfo = notification.getTransferInformation();
        if (encryptedCapitalTransferInfo != null) {
            String retirementFundUid = employmentTermination.get().getEmploymentInfo().getRetirementFundUid();
            fullNotification.setTransferInformation(new CapitalTransferInfoEncrypter().decrypt(encryptedCapitalTransferInfo, nodeConfigService.getPrivateKey(retirementFundUid)));
        }

        listener.handleMatchForTerminationNotification(fullNotification);
    }

    public Optional<EncryptedData> handleTerminationMatch(MatchForCommencement notification)
            throws NotificationException {
        final Optional<EmploymentCommencement> employmentCommencement = employmentCommencementProvider.getEmploymentCommencements().stream()
                .filter(currentEmploymentCommencement -> isSameAsNotification(currentEmploymentCommencement, notification))
                .findAny();

        if (!employmentCommencement.isPresent()) {
            LOG.warn("Termination notification received which does not correlate with any employment start: {}", notification);
            return Optional.empty();
        }

        final FullMatchForCommencementNotification fullNotification = new FullMatchForCommencementNotification();
        fullNotification.setPreviousRetirementFundUid(notification.getPreviousRetirementFundUid());
        fullNotification.setTerminationDate(notification.getTerminationDate());
        fullNotification.setEmploymentCommencement(employmentCommencement.get());

        listener.handleMatchForCommencementNotification(fullNotification);

        CapitalTransferInformation info = employmentCommencement.get().getCapitalTransferInfo();
        if (info == null) {
            return Optional.empty();
        }

        return Optional.of(new CapitalTransferInfoEncrypter().encrypt(info, nodeConfigService.getPublicKey(notification.getPreviousRetirementFundUid())));
    }

    private boolean isSameAsNotification(EmploymentCommencement employmentCommencement, MatchForCommencement notification) {
        EmploymentInfo employmentInfo = employmentCommencement.getEmploymentInfo();
        return Cryptography.digestOasiNumber(employmentInfo.getOasiNumber()).equals(notification.getEncryptedOasiNumber()) &&
                employmentInfo.getRetirementFundUid().equals(notification.getNewRetirementFundUid()) &&
                employmentInfo.getDate().equals(notification.getCommencementDate());
    }

    private boolean isSameAsNotification(EmploymentTermination employmentTermination, MatchForTermination notification) {
        EmploymentInfo employmentInfo = employmentTermination.getEmploymentInfo();
        return Cryptography.digestOasiNumber(employmentInfo.getOasiNumber()).equals(notification.getEncryptedOasiNumber()) &&
                employmentInfo.getRetirementFundUid().equals(notification.getPreviousRetirementFundUid()) &&
                employmentInfo.getDate().equals(notification.getTerminationDate());
    }
}

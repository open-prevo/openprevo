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
package ch.prevo.open.hub.nodes;

import ch.prevo.open.encrypted.model.EncryptedData;
import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.encrypted.model.MatchForTermination;
import ch.prevo.open.hub.match.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@Service
public class NodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeService.class);

    private final NodeRegistry nodeRegistry;

    private final NodeCaller nodeCaller;

    @Inject
    public NodeService(NodeRegistry nodeRegistry, NodeCaller nodeCaller) {
        this.nodeRegistry = nodeRegistry;
        this.nodeCaller = nodeCaller;
    }

    public Set<InsurantInformation> getCurrentTerminations() {
        Set<InsurantInformation> exits = new HashSet<>();
        for (NodeConfiguration nodeConfig : nodeRegistry.getCurrentNodes()) {
            List<InsurantInformation> pensionFundExits = nodeCaller
                    .getInsurantInformationList(nodeConfig.getEmploymentTerminationsUrl());
            List<InsurantInformation> filteredInformation = filterInvalidInsurantInformation(nodeConfig,
                    pensionFundExits);
            exits.addAll(filteredInformation);
        }
        return exits;
    }

    public Set<InsurantInformation> getCurrentCommencements() {
        Set<InsurantInformation> entries = new HashSet<>();
        for (NodeConfiguration nodeConfig : nodeRegistry.getCurrentNodes()) {
            List<InsurantInformation> pensionFundEntries = nodeCaller
                    .getInsurantInformationList(nodeConfig.getEmploymentCommencementsUrl());
            List<InsurantInformation> filteredInformation = filterInvalidInsurantInformation(nodeConfig,
                    pensionFundEntries);
            entries.addAll(filteredInformation);
        }
        return entries;
    }

    private List<InsurantInformation> filterInvalidInsurantInformation(NodeConfiguration nodeConfiguration,
                                                                            List<InsurantInformation> insurantInformation) {
        List<InsurantInformation> invalidMatches = verifyInsurantInformationOnlyBelongsToThisNode(nodeConfiguration,
                insurantInformation);
        return insurantInformation.stream()
                .filter(((Predicate<InsurantInformation>) invalidMatches::contains).negate())
                .collect(toList());
    }

    private List<InsurantInformation> verifyInsurantInformationOnlyBelongsToThisNode(NodeConfiguration nodeConfig,
                                                                                     List<InsurantInformation> pensionFundExits) {
        List<InsurantInformation> invalidInsurants = pensionFundExits.stream()
                .filter(insurant -> !nodeConfig.containsRetirementFundUid(insurant.getRetirementFundUid()))
                .collect(toList());

        if (!invalidInsurants.isEmpty()) {
            LOGGER.error(
                    "Invalid data received from node {} the following insurants have an invalid retirement fund {}",
                    nodeConfig, invalidInsurants);
        }
        return invalidInsurants;
    }

    public void notifyMatches(List<Match> matches) {
        List<NodeConfiguration> nodeConfigurations = nodeRegistry.getCurrentNodes();

        for (Match match : matches) {
            try {
                // notify new node
                final EncryptedData transferInformation = tryNotifyNewRetirementFundAboutMatch(
                        findNodeToNotify(match.getNewRetirementFundUid(), nodeConfigurations), match);
                if (transferInformation != null) {
                    // notify previous node
                    tryNotifyPreviousRetirementFundAboutTerminationMatch(
                            findNodeToNotify(match.getPreviousRetirementFundUid(), nodeConfigurations), match,
                            transferInformation);
                }
            } catch (Exception e) {
                LOGGER.error("Unexpected error occurred while notifying match: {}", match, e);
            }
        }
    }

    private EncryptedData tryNotifyNewRetirementFundAboutMatch(NodeConfiguration nodeConfig, Match match) {
        MatchForCommencement matchNotification = new MatchForCommencement();
        matchNotification.setEncryptedOasiNumber(match.getEncryptedOasiNumber());
        matchNotification.setNewRetirementFundUid(match.getNewRetirementFundUid());
        matchNotification.setPreviousRetirementFundUid(match.getPreviousRetirementFundUid());
        matchNotification.setCommencementDate(match.getEntryDate());
        matchNotification.setTerminationDate(match.getExitDate());
        return nodeCaller.postCommencementNotification(nodeConfig.getCommencementMatchNotifyUrl(), matchNotification);
    }

    private void tryNotifyPreviousRetirementFundAboutTerminationMatch(NodeConfiguration nodeConfig,
                                                                      Match match,
                                                                      EncryptedData transferInformation) {
        MatchForTermination matchNotification = new MatchForTermination();
        matchNotification.setEncryptedOasiNumber(match.getEncryptedOasiNumber());
        matchNotification.setPreviousRetirementFundUid(match.getPreviousRetirementFundUid());
        matchNotification.setNewRetirementFundUid(match.getNewRetirementFundUid());
        matchNotification.setCommencementDate(match.getEntryDate());
        matchNotification.setTerminationDate(match.getExitDate());
        matchNotification.setTransferInformation(transferInformation);
        String terminationMatchNotifyUrl = nodeConfig.getTerminationMatchNotifyUrl();
        nodeCaller.postTerminationNotification(terminationMatchNotifyUrl, matchNotification);
    }

    private NodeConfiguration findNodeToNotify(String retirementFundUid, List<NodeConfiguration> nodeConfigurations) {

        return nodeConfigurations.stream()
                .filter(n -> n.containsRetirementFundUid(retirementFundUid)).findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Did not find a node for retirement fund UID: " + retirementFundUid));
    }
}

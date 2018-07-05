package ch.prevo.open.hub.nodes;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.model.MatchNotification;
import ch.prevo.open.hub.match.Match;
import ch.prevo.open.hub.match.MatcherService;

@Service
public class NodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeService.class);

    @Inject
    private NodeRegistry nodeRegistry;

    @Inject
    private MatcherService matcherService;

    private final RestTemplate restTemplate;

    public NodeService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Set<InsurantInformation> getCurrentExits() {
        Set<InsurantInformation> exits = new HashSet<>();
        for (NodeConfiguration nodeConfig : nodeRegistry.getCurrentNodes()) {
            List<InsurantInformation> pensionFundExits = lookupInsurantInformationList(nodeConfig.getJobExitsUrl());
            List<InsurantInformation> invalidInsurants = verifyInsurantInformationOnlyBelongsToThisNode(nodeConfig,
                    pensionFundExits);
            exits.addAll(filterMatches(pensionFundExits, invalidInsurants,
                    matcherService::employmentCommencementNotMatched));
        }
        return exits;
    }

    public Set<InsurantInformation> getCurrentEntries() {
        Set<InsurantInformation> entries = new HashSet<>();
        for (NodeConfiguration nodeConfig : nodeRegistry.getCurrentNodes()) {
            List<InsurantInformation> pensionFundEntries = lookupInsurantInformationList(nodeConfig.getJobEntriesUrl());
            List<InsurantInformation> invalidInsurants = verifyInsurantInformationOnlyBelongsToThisNode(nodeConfig,
                    pensionFundEntries);
            entries.addAll(filterMatches(pensionFundEntries, invalidInsurants,
                    matcherService::employmentTerminationNotMatched));
        }
        return entries;
    }

    private List<InsurantInformation> filterMatches(List<InsurantInformation> insurantInformation,
                                                    List<InsurantInformation> invalidMatches,
                                                    Predicate<InsurantInformation> alreadyMatched) {
        return insurantInformation.stream()
                .filter(((Predicate<InsurantInformation>)invalidMatches::contains).negate())
                .filter(alreadyMatched)
                .collect(toList());
    }

    private List<InsurantInformation> lookupInsurantInformationList(String url) {
        try {
            InsurantInformation[] nodeExits = restTemplate.getForObject(url, InsurantInformation[].class);
            return nodeExits == null ? emptyList() : asList(nodeExits);
        } catch (Exception e) {
            LOGGER.error("Could not fetch data from URL {}", url, e);
        }
        return emptyList();
    }

    private List<InsurantInformation> verifyInsurantInformationOnlyBelongsToThisNode(NodeConfiguration nodeConfig,
                                                                                     List<InsurantInformation> pensionFundExits) {
        List<InsurantInformation> invalidInsurants = pensionFundExits.stream()
                .filter(insurant -> !nodeConfig.containsRetirementFundUid(insurant.getRetirementFundUid()))
                .collect(toList());

        if (invalidInsurants.size() > 0) {
            LOGGER.error("Invalid data received from node {} the following insurants have an invalid retirement fund",
                    nodeConfig, invalidInsurants);
        }
        return invalidInsurants;
    }

    public void notifyMatches(List<Match> matches) {
        List<NodeConfiguration> nodeConfigurations = nodeRegistry.getCurrentNodes();

        for (Match match : matches) {
            try {
                // notify previous node
                tryNotifyMatch(findNodeToNotify(match.getPreviousRetirementFundUid(), nodeConfigurations), match);
                // notify new node
                tryNotifyMatch(findNodeToNotify(match.getNewRetirementFundUid(), nodeConfigurations), match);
            } catch (Exception e) {
                LOGGER.error("Unexpected error occurred while notifying match: {}", match, e);
            }
        }
    }

    private NodeConfiguration findNodeToNotify(String retirementFundUid, List<NodeConfiguration> nodeConfigurations) {

        return nodeConfigurations.stream()
                .filter(n -> n.containsRetirementFundUid(retirementFundUid)).findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private void tryNotifyMatch(NodeConfiguration nodeConfig, Match match) {
        try {
            MatchNotification matchNotification = new MatchNotification();
            matchNotification.setEncryptedOasiNumber(match.getEncryptedOasiNumber());
            matchNotification.setNewRetirementFundUid(match.getNewRetirementFundUid());
            restTemplate.postForEntity(nodeConfig.getMatchNotifyUrl(), matchNotification, String.class);
        } catch (Exception e) {
            // TODO persist information that match needs to be notified later
            LOGGER.error("Could not send notification for match {} to URL {}", match, nodeConfig.getMatchNotifyUrl(),
                    e);
        }
    }
}

package ch.prevo.open.hub.nodes;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.model.MatchNotification;
import ch.prevo.open.hub.match.Match;

@Service
public class NodeService {

    private static Logger LOGGER = LoggerFactory.getLogger(NodeService.class);

    @Inject
    private NodeRegistry nodeRegistry;

    private final RestTemplate restTemplate;

    public NodeService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Set<InsurantInformation> getCurrentExits() {
        Set<InsurantInformation> exits = new HashSet<>();
        for (NodeConfiguration nodeConfig : nodeRegistry.currentNodes()) {
            exits.addAll(lookupInsurantInformationList(nodeConfig.getJobExitsUrl()));
        }
        return exits;
    }

    public Set<InsurantInformation> getCurrentEntries() {
        Set<InsurantInformation> exits = new HashSet<>();
        for (NodeConfiguration nodeConfig : nodeRegistry.currentNodes()) {
            exits.addAll(lookupInsurantInformationList(nodeConfig.getJobEntriesUrl()));
        }
        return exits;
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

    public void notifyMatches(List<Match> matches) {
        List<NodeConfiguration> nodeConfigurations = nodeRegistry.currentNodes();

        for (Match match : matches) {
            try {
                Pair<NodeConfiguration, NodeConfiguration> nodesToNotify = findNodesToNotify(match, nodeConfigurations);

                // notify previous node
                tryNotifyMatch(nodesToNotify.getLeft(), match);
                tryNotifyMatch(nodesToNotify.getRight(), match);
            } catch (Exception e) {
                LOGGER.error("Unexpected error occurred while notifying match: {}", match, e);
            }
        }
    }

    private Pair<NodeConfiguration, NodeConfiguration> findNodesToNotify(Match match,
                                                                         List<NodeConfiguration> configurations) {
        NodeConfiguration nodeOfPreviousRetirementFund = null;
        NodeConfiguration nodeOfNewRetirementFund = null;

        for (NodeConfiguration configuration : configurations) {
            if (configuration.containsRetirementFundUid(match.getPreviousRetirementFundUid())) {
                nodeOfPreviousRetirementFund = configuration;
            } else if (configuration.containsRetirementFundUid(match.getNewRetirementFundUid())) {
                nodeOfNewRetirementFund = configuration;
            }
        }

        if (nodeOfPreviousRetirementFund != null && nodeOfNewRetirementFund != null) {
            return new ImmutablePair<>(nodeOfPreviousRetirementFund, nodeOfNewRetirementFund);
        } else {
            throw new IllegalStateException("Could not find both nodes containing retirement funds for match " + match);
        }
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

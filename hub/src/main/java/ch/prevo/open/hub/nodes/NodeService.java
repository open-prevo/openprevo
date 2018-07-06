package ch.prevo.open.hub.nodes;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.model.MatchNotification;
import ch.prevo.open.hub.match.Match;
import ch.prevo.open.hub.match.MatcherService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Service
public class NodeService {

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
            exits.addAll(pensionFundExits.stream().filter(matcherService::employmentCommencementNotMatched).collect(toList()));
        }
        return exits;
    }

    public Set<InsurantInformation> getCurrentEntries() {
        Set<InsurantInformation> entries = new HashSet<>();
        for (NodeConfiguration nodeConfig : nodeRegistry.getCurrentNodes()) {
            List<InsurantInformation> pensionFundEntries = lookupInsurantInformationList(nodeConfig.getJobEntriesUrl());
            entries.addAll(pensionFundEntries.stream().filter(matcherService::employmentTerminationNotMatched).collect(toList()));
        }
        return entries;
    }

    private List<InsurantInformation> lookupInsurantInformationList(String url) {
        InsurantInformation[] nodeExits = restTemplate.getForObject(url, InsurantInformation[].class);
        return nodeExits == null ? emptyList() : asList(nodeExits);
    }

    public void notifyMatches(List<Match> matches) {
        for (NodeConfiguration nodeConfig : nodeRegistry.getCurrentNodes()) {
            notifyMatches(nodeConfig, matches);
        }
    }

    private void notifyMatches(NodeConfiguration nodeConfig, List<Match> matches) {
        for (Match match : matches) {
            if (nodeConfig.containsRetirementFundUid(match.getPreviousRetirementFundUid())
                    || nodeConfig.containsRetirementFundUid(match.getNewRetirementFundUid())) {
                MatchNotification matchNotification = new MatchNotification();
                matchNotification.setEncryptedOasiNumber(match.getEncryptedOasiNumber());
                matchNotification.setNewRetirementFundUid(match.getNewRetirementFundUid());
                restTemplate.postForEntity(nodeConfig.getMatchNotifyUrl(), matchNotification, String.class);
            }
        }
    }
}

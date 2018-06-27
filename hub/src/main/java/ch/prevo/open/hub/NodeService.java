package ch.prevo.open.hub;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.hub.match.Match;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@Service
public class NodeService {

    @Inject
    private NodeRegistry nodeRegistry;

    private final RestTemplate restTemplate;

    public NodeService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Set<InsurantInformation> currentExits() {
        Set<InsurantInformation> exits = new HashSet<>();
        for (NodeConfiguration nodeConfig : nodeRegistry.currentNodes()) {
            exits.addAll(lookupInsurantInformationList(nodeConfig.getJobExitsUrl()));
        }
        return exits;
    }

    public Set<InsurantInformation> currentEntries() {
        Set<InsurantInformation> exits = new HashSet<>();
        for (NodeConfiguration nodeConfig : nodeRegistry.currentNodes()) {
            exits.addAll(lookupInsurantInformationList(nodeConfig.getJobEntriesUrl()));
        }
        return exits;
    }

    private List<InsurantInformation> lookupInsurantInformationList(String url) {
        InsurantInformation[] nodeExits = restTemplate.getForObject(url, InsurantInformation[].class);
        return nodeExits == null ? emptyList() : asList(nodeExits);
    }

    public void notifyMatches(List<Match> matches) {
        for (NodeConfiguration nodeConfig : nodeRegistry.currentNodes()) {
            notifyMatches(nodeConfig, matches);
        }
    }

    private void notifyMatches(NodeConfiguration nodeConfig, List<Match> matches) {
        for (Match match : matches) {
            if (match.getExit().getRetirementFundUid().equals(nodeConfig.getUid())
                    || match.getEntry().getRetirementFundUid().equals(nodeConfig.getUid())) {
                restTemplate.postForEntity(nodeConfig.getMatchNotifyUrl(), match, null);
            }
        }
    }
}

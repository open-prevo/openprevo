package ch.prevo.open.hub;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.hub.match.Match;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

@Service
public class NodeService {

    public static final String JOB_END_ENDPOINT_NAME = "job-end";
    public static final String JOB_START_ENDPOINT_NAME = "job-start";
    public static final String MATCH_NOTIFY_ENDPOINT_NAME = "match-notify";

    private final RestTemplate restTemplate;

    // Open
    // - registry of known node instances

    public NodeService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Set<InsurantInformation> currentExits() {
        InsurantInformation[] result = restTemplate.getForObject("/" + JOB_END_ENDPOINT_NAME, InsurantInformation[].class);
        return new HashSet<>(asList(result));
    }

    public Set<InsurantInformation> currentEntries() {
        InsurantInformation[] result = restTemplate.getForObject("/" + JOB_START_ENDPOINT_NAME, InsurantInformation[].class);
        return new HashSet<>(asList(result));
    }

    public void notifyMatches(List<Match> matches) {
        restTemplate.postForEntity("/" + MATCH_NOTIFY_ENDPOINT_NAME, matches, null);
    }
}

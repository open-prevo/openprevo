package ch.prevo.open.hub.nodes;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * All node instances currently known by the hub, currently only a mock implementation.
 */
@Service
@Profile("!docker")
class MockNodeRegistry implements NodeRegistry {

    static final NodeConfiguration NODE_1 =
            new NodeConfiguration("https://host1/job-exits",
                    "https://host1/job-entries",
                    "https://host1/match-notify",
                    asList("CHE-223.471.073", "CHE-109.713.957"));
    static final NodeConfiguration NODE_2 =
            new NodeConfiguration("https://host2/job-exits",
                    "https://host2/job-entries",
                    "https://host2/match-notify",
                    singletonList("CHE-109.723.097"));

    @Override
    public List<NodeConfiguration> getCurrentNodes() {
        return asList(NODE_1, NODE_2);
    }
}

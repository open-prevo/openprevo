package ch.prevo.open.hub.nodes;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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
                    "CHE-223.471.073", "CHE-109.713.957");
    static final NodeConfiguration NODE_2 =
            new NodeConfiguration("https://host2/job-exits",
                    "https://host2/job-entries",
                    "https://host2/match-notify",
                    "CHE-109.723.097");

    @Override
    public List<NodeConfiguration> getCurrentNodes() {
        return Arrays.asList(NODE_1, NODE_2);
    }
}

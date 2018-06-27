package ch.prevo.open.hub;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * All node instances currently known by the hub, currently only a mock implementation.
 */
@Service
public class NodeRegistry {

    public static final NodeConfiguration NODE_1 =
            new NodeConfiguration("CHE-223.471.073", "https://host1/job-exits", "https://host1/job-entries", "https://host1/match-notify");
    public static final NodeConfiguration NODE_2 =
            new NodeConfiguration("CHE-109.723.097", "https://host2/job-exits", "https://host2/job-entries", "https://host2/match-notify");

    public List<NodeConfiguration> currentNodes() {
        return Arrays.asList(NODE_1, NODE_2);
    }
}

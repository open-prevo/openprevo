package ch.prevo.open.hub.nodes;

import static java.util.Collections.singletonList;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * All node instances currently known by the hub, currently only a mock implementation.
 */
@Service
@Profile("docker")
class DockerNodeRegistry implements NodeRegistry {

    static final NodeConfiguration BALOISE_NODE =
            new NodeConfiguration("http://node_baloise:8080/job-end",
                    singletonList("CHE-109.740.084-Baloise-Sammelstiftung"));
    static final NodeConfiguration HELVETIA_NODE =
            new NodeConfiguration("http://node_helvetia:8080/job-end",
                    singletonList("CHE-109.537.488-Helvetia-Prisma-Sammelstiftung"));

    @Override
    public List<NodeConfiguration> getCurrentNodes() {
        return Arrays.asList(BALOISE_NODE, HELVETIA_NODE);
    }
}

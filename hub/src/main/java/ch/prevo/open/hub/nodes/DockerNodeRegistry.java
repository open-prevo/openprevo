package ch.prevo.open.hub.nodes;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * All node instances currently known by the hub, currently only a mock implementation.
 */
@Service
@Profile("docker")
class DockerNodeRegistry implements NodeRegistry {

    static final NodeConfiguration BALOISE_NODE =
            new NodeConfiguration("http://node_baloise:8080",
                    "CHE-109.740.084-Baloise-Sammelstiftung", "CHE-109.740.084-Baloise-Sammelstiftung 2");
    static final NodeConfiguration HELVETIA_NODE =
            new NodeConfiguration("http://node_helvetia:8080",
                    "CHE-109.537.488-Helvetia-Prisma-Sammelstiftung", "CHE-109.537.488-Helvetia-Prisma-Sammelstiftung 2");

    @Override
    public List<NodeConfiguration> getCurrentNodes() {
        return Arrays.asList(BALOISE_NODE, HELVETIA_NODE);
    }
}

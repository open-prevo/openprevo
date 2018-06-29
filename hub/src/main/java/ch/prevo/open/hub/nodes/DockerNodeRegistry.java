package ch.prevo.open.hub.nodes;

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

//    static final NodeConfiguration BALOISE_NODE =
//            new NodeConfiguration("http://localhost:8881/job-end",
//                    "http://localhost:8881/job-start",
//                    "http://localhost:8881/match-notification",
//                    "CHE-109.740.084-Baloise-Sammelstiftung");
//    static final NodeConfiguration HELVETIA_NODE =
//            new NodeConfiguration("http://localhost:8882/job-end",
//                    "http://localhost:8882/job-start",
//                    "http://localhost:8882/match-notification",
//                    "CHE-109.537.488-Helvetia-Prisma-Sammelstiftung");


    static final NodeConfiguration BALOISE_NODE =
            new NodeConfiguration("http://ve_node:8080/job-end",
                    "http://ve_node:8080/job-start",
                    "http://ve_node:8080/match-notification",
                    "CHE-109.740.084-Baloise-Sammelstiftung");
    static final NodeConfiguration HELVETIA_NODE =
            new NodeConfiguration("http://pakt_node:8080/job-end",
                    "http://pakt_node:8080/job-start",
                    "http://pakt_node:8080/match-notification",
                    "CHE-109.537.488-Helvetia-Prisma-Sammelstiftung");

    @Override
    public List<NodeConfiguration> getCurrentNodes() {
        return Arrays.asList(BALOISE_NODE, HELVETIA_NODE);
    }
}

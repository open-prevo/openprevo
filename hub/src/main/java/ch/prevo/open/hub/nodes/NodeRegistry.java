package ch.prevo.open.hub.nodes;

import java.util.List;

public interface NodeRegistry {
    /**
     * Provide all currently registered nodes.
     *
     * @return The configuration for each node.
     */
    List<NodeConfiguration> getCurrentNodes();
}

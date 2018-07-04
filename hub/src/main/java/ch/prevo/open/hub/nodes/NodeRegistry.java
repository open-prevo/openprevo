package ch.prevo.open.hub.nodes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

/**
 * All node instances currently known by the hub, currently only a mock implementation.
 */
@Service
class NodeRegistry {

    private static Logger LOG = LoggerFactory.getLogger(NodeRegistry.class);

    private final ObjectMapper mapper;

    private List<NodeConfiguration> nodes = Collections.emptyList();

    @Inject
    public NodeRegistry(@Named("yamlMapper") ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @PostConstruct
    public void init() {
        try (final InputStream yaml = NodeRegistry.class.getResourceAsStream("nodes.yaml")) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(yaml, "UTF-8"));
            nodes = mapper.readValue(reader, new TypeReference<List<NodeConfiguration>>() {});
        } catch (IOException e) {
            LOG.warn("Unable to read bootstrap-data from nodes.yaml", e);
        }
    }

    List<NodeConfiguration> currentNodes() {
        return nodes;
    }
}

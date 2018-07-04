package ch.prevo.open.hub.nodes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * All node instances currently known by the hub, currently only a mock implementation.
 */
@Service
class NodeRegistry {

    private static Logger LOG = LoggerFactory.getLogger(NodeRegistry.class);

    private final ResourceLoader loader;
    private final ObjectMapper mapper;

    @Value("${open.prevo.hub.config.file}")
    private String configFile;

    private List<NodeConfiguration> nodes = Collections.emptyList();

    @Inject
    public NodeRegistry(ResourceLoader loader, @Named("yamlMapper") ObjectMapper mapper) {
        this.loader = loader;
        this.mapper = mapper;
    }

    @PostConstruct
    public void init() {
        try {
            final Resource resource = loader.getResource(configFile);
            nodes = mapper.readValue(resource.getInputStream(), new TypeReference<List<NodeConfiguration>>() {});
        } catch (IOException e) {
            LOG.warn("Unable to read bootstrap-data from " + configFile, e);
        }
    }

    List<NodeConfiguration> currentNodes() {
        return nodes;
    }
}

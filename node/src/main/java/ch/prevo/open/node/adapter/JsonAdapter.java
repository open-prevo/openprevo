package ch.prevo.open.node.adapter;

import static java.util.Collections.emptyList;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.prevo.open.encrypted.model.InsurantInformation;

/**
 * Sample json dummy adapter to provide hardcoded test data.
 */
@Repository
public final class JsonAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonAdapter.class);

    @Value("${open.prevo.json.adapter.files.jobstart}")
    private String jobStartJsonFile;

    @Value("${open.prevo.json.adapter.files.jobend}")
    private String jobEndJsonFile;

    @Inject
    private ResourceLoader resourceLoader;

    @Inject
    private ObjectMapper objectMapper;

    private Set<InsurantInformation> jobStartInformation;
    private Set<InsurantInformation> jobEndInformation;

    @PostConstruct
    public void init() {
        this.jobStartInformation = new HashSet<>(readJsonFile(jobStartJsonFile));
        this.jobEndInformation = new HashSet<>(readJsonFile(jobEndJsonFile));
    }

    private List<InsurantInformation> readJsonFile(String filePath) {
        LOGGER.debug("Read json file {} in adapter", filePath);
        try {
            Resource jsonResource = resourceLoader.getResource(filePath);
            String jsonString = IOUtils.toString(jsonResource.getInputStream(), Charset.forName("UTF-8"));
            InsurantInformation[] insurantInformation = objectMapper
                    .readValue(jsonString, InsurantInformation[].class);
            return Arrays.asList(insurantInformation);
        } catch (Exception e) {
            LOGGER.error("Could not initialize dummy json adapter", e);
        }
        return emptyList();
    }

    public Set<InsurantInformation> getJobStartInformation() {
        return jobStartInformation;
    }

    public Set<InsurantInformation> getJobEndInformation() {
        return jobEndInformation;
    }
}

package ch.prevo.open.node.data.provider;

import static java.util.Collections.emptyList;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobStart;

/**
 * Sample json dummy adapter to provide hardcoded test data.
 */
@Repository
public final class JsonAdapter implements JobStartProvider, JobEndProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonAdapter.class);

    @Value("${open.prevo.json.adapter.files.jobstart}")
    private String jobStartJsonFile;

    @Value("${open.prevo.json.adapter.files.jobend}")
    private String jobEndJsonFile;

    @Inject
    private ResourceLoader resourceLoader;

    @Inject
    private ObjectMapper objectMapper;

    private List<JobStart> jobStartInformation;
    private List<JobEnd> jobEndInformation;

    @PostConstruct
    public void init() {
        this.jobStartInformation = new ArrayList<>(readJsonFile(jobStartJsonFile, new TypeReference<List<JobStart>>() {}));
        this.jobEndInformation = new ArrayList<>();// new ArrayList<>(readJsonFile(jobEndJsonFile, new TypeReference<List<JobEnd>>() {}));
    }

    private <T> List<T> readJsonFile(String filePath, TypeReference<List<T>> typeReference) {
        LOGGER.debug("Read json file {} in adapter", filePath);
        try {
            Resource jsonResource = resourceLoader.getResource(filePath);
            String jsonString = IOUtils.toString(jsonResource.getInputStream(), Charset.forName("UTF-8"));

            return objectMapper
                    .readValue(jsonString, typeReference);
        } catch (Exception e) {
            LOGGER.error("Could not initialize dummy json adapter", e);
        }
        return emptyList();
    }

    @Override
    public List<JobStart> getJobStarts() {
        return jobStartInformation;
    }

    @Override
    public List<JobEnd> getJobEnds() {
        return jobEndInformation;
    }
}

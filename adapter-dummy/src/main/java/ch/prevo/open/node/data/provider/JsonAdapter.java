package ch.prevo.open.node.data.provider;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobStart;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Sample json dummy adapter to provide hardcoded test data.
 */
@EnableAutoConfiguration
@Repository
public class JsonAdapter implements JobStartProvider, JobEndProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonAdapter.class);

    private ObjectMapper objectMapper;
    private ResourceLoader resourceLoader;
    private String jobStartJsonFile;
    private String jobEndJsonFile;
    private List<JobStart> jobStartInformation;
    private List<JobEnd> jobEndInformation;

    @Inject
    public JsonAdapter(ObjectMapper objectMapper,
                       ResourceLoader resourceLoader,
                       @Value("${open.prevo.json.adapter.files.jobstart}") String jobStartJsonFile,
                       @Value("${open.prevo.json.adapter.files.jobend}") String jobEndJsonFile) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        this.jobStartJsonFile = jobStartJsonFile;
        this.jobEndJsonFile = jobEndJsonFile;
    }

    @PostConstruct
    public void init() {
        this.jobStartInformation = new ArrayList<>(readJsonFile(jobStartJsonFile, JobStart.class));
        this.jobEndInformation = new ArrayList<>(readJsonFile(jobEndJsonFile, JobEnd.class));
    }

    private <T> List<T> readJsonFile(String filePath, Class<T> clazz) {
        LOGGER.debug("Read json file {} in adapter", filePath);
        try {
            Resource jsonResource = resourceLoader.getResource(filePath);
            String jsonString = IOUtils.toString(jsonResource.getInputStream(), Charset.forName("UTF-8"));
            JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
            return objectMapper.readValue(jsonString, type);
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

package ch.prevo.open.node.data.provider;

import static java.util.Collections.emptyList;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.data.api.EmploymentCommencement;

/**
 * Sample json dummy adapter to provide hardcoded test data.
 */
public class JsonAdapter implements EmploymentCommencementProvider, EmploymentTerminationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonAdapter.class);

    private ObjectMapper objectMapper;
    private ResourceLoader resourceLoader;
    private String employmentCommencementJsonFile;
    private String employmentTerminationJsonFile;
    private List<EmploymentCommencement> employmentCommencementInformation;
    private List<EmploymentTermination> employmentTerminationInformation;

    public JsonAdapter() {
        this(getEmploymentCommencementJsonFile(), getEmploymentTerminationJsonFile());
    }

    public JsonAdapter(String employmentCommencementJsonFile, String employmentTerminationJsonFile) {
        this.employmentCommencementJsonFile = employmentCommencementJsonFile;
        this.employmentTerminationJsonFile = employmentTerminationJsonFile;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
        this.resourceLoader = new ResourceLoader();
        this.readJsonFiles();
    }

    private void readJsonFiles() {
        this.employmentCommencementInformation = new ArrayList<>(
                readJsonFile(employmentCommencementJsonFile, EmploymentCommencement.class));
        this.employmentTerminationInformation = new ArrayList<>(
                readJsonFile(employmentTerminationJsonFile, EmploymentTermination.class));
    }

    private <T> List<T> readJsonFile(String filePath, Class<T> clazz) {
        LOGGER.debug("Read json file {} in adapter", filePath);
        try {
            InputStream jsonStream = resourceLoader.getResource(filePath);
            String jsonString = IOUtils.toString(jsonStream, Charset.forName("UTF-8"));
            JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
            return objectMapper.readValue(jsonString, type);
        } catch (Exception e) {
            LOGGER.error("Could not initialize dummy json adapter", e);
        }
        return emptyList();
    }

    @Override
    public List<EmploymentCommencement> getEmploymentCommencements() {
        return employmentCommencementInformation;
    }

    @Override
    public List<EmploymentTermination> getEmploymentTerminations() {
        return employmentTerminationInformation;
    }

    private static String getEmploymentCommencementJsonFile() {
        String employmentCommencementJson = System.getenv("EMPLOYMENT_COMMENCEMENT_JSON");
        if (employmentCommencementJson == null) {
            employmentCommencementJson = "classpath:sample-employment-commencement.json";
        }
        return employmentCommencementJson;
    }

    private static String getEmploymentTerminationJsonFile() {
        String employmentTerminationJson = System.getenv("EMPLOYMENT_TERMINATION_JSON");
        if (employmentTerminationJson == null) {
            employmentTerminationJson = "classpath:sample-employment-termination.json";
        }
        return employmentTerminationJson;
    }
}

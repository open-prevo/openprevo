package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import org.apache.commons.io.FileUtils;
import org.apache.poi.EmptyFileException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ch.prevo.open.node.adapter.excel.ExcelConstants.COMMENCEMENTS_LABEL;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.TERMINATION_LABEL;

public class ExcelProvider implements EmploymentCommencementProvider, EmploymentTerminationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReader.class);

    public static final String FILE_PROPERTY = "node.adapter.excel.in.file";
    private static final String FALLBACK_FILE = "retirement-fund-test-data_de.xlsx";

    private final ExcelReader reader = new ExcelReader();

    @Override
    public List<EmploymentTermination> getEmploymentTerminations() {
        List<EmploymentTermination> employmentTerminations = Collections.emptyList();
        try (final Workbook wb = getWorkbook()) {
            if (wb != null) {
                employmentTerminations = reader.mapEmploymentTerminationList(wb.getSheet(TERMINATION_LABEL));
            }
        } catch (IOException e) {
            LOGGER.error("An exception occurred while trying to read the employment terminations", e);
        }
        return employmentTerminations;
    }

    @Override
    public void notifyTerminationErrors(Map<EmploymentTermination, Set<ConstraintViolation<EmploymentTermination>>> violations) {
        final String filename = getErrorFilename();
        final Workbook workbook = getErrorWorkbook(filename);

        if (workbook != null) {
            try (final ErrorWriter writer = new ErrorWriter(workbook, filename)) {
                writer.markTerminationErrors(violations);
            } catch (IOException e) {
                LOGGER.error("An exception occurred while trying to read the employment terminations", e);
            }
        }
    }

    @Override
    public List<EmploymentCommencement> getEmploymentCommencements() {
        List<EmploymentCommencement> employmentCommencements = Collections.emptyList();
        try (final Workbook wb = getWorkbook()) {
            if (wb != null) {
                employmentCommencements = reader.mapEmploymentCommencementList(wb.getSheet(COMMENCEMENTS_LABEL));
            }
        } catch (IOException e) {
            LOGGER.error("An exception occurred while trying to read the employment commencements", e);
        }
        return employmentCommencements;
    }

    @Override
    public void notifyCommencementErrors(Map<EmploymentCommencement, Set<ConstraintViolation<EmploymentCommencement>>> violations) {
        final String filename = getErrorFilename();
        final Workbook workbook = getErrorWorkbook(filename);

        if (workbook != null) {
            try (final ErrorWriter writer = new ErrorWriter(workbook, filename)) {
                writer.markCommencementErrors(violations);
            } catch (IOException e) {
                LOGGER.error("An exception occurred while trying to read the employment terminations", e);
            }
        }
    }

    private Workbook getErrorWorkbook(String filename) {
        final Path path = Paths.get(filename);
        if (Files.exists(path)) {
            try (final InputStream inputStream = new FileInputStream(filename)) {
                return WorkbookFactory.create(inputStream);
            } catch (IOException | EncryptedDocumentException | EmptyFileException | InvalidFormatException e) {
                LOGGER.warn("Unable to read existing error file {}, creating a new one", filename);
                FileUtils.deleteQuietly(new File(filename));
            }
        }

        final Workbook workbook = getWorkbook();
        if (workbook == null) {
            LOGGER.error("Unable to read input file");
            return null;
        }
        return workbook;
    }

    private static String getErrorFilename() {
        final String filenamePrefix = System.getProperty(FILE_PROPERTY, FALLBACK_FILE);
        return (filenamePrefix.endsWith(".xlsx") ? filenamePrefix.substring(0, filenamePrefix.length() - 5) : filenamePrefix) + "-error.xlsx";
    }

    private Workbook getWorkbook() {
        try (final InputStream inputStream = getFileInput()) {
            return WorkbookFactory.create(inputStream);
        } catch (IOException | EncryptedDocumentException | EmptyFileException | InvalidFormatException e) {
            LOGGER.error("An exception occurred while trying to get the workbook", e);
            return null;
        }
    }

    private InputStream getFileInput() throws FileNotFoundException {
        final String excelFilePath = System.getProperty(FILE_PROPERTY);
        return excelFilePath != null ?
                new FileInputStream(excelFilePath)
                : ExcelReader.class.getClassLoader().getResourceAsStream(FALLBACK_FILE);
    }


}

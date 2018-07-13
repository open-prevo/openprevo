package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.data.api.JobStart;
import ch.prevo.open.encrypted.model.Address;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.node.data.provider.JobEndProvider;
import ch.prevo.open.node.data.provider.JobStartProvider;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ExcelReader implements JobStartProvider, JobEndProvider {

    private static Logger LOG = LoggerFactory.getLogger(ExcelReader.class);

    private static final int OASI_COLUMN_INDEX = 0;
    private static final int DATE_COLUMN_INDEX = 1;
    private static final int RETIREMENT_FUND_UID_COLUMN_INDEX = 2;
    private static final int REFERENCE_COLUMN_INDEX = 3;
    private static final int NAME_COLUMN_INDEX = 4;
    private static final int ADDITIONAL_NAME_COLUMN_INDEX = 5;
    private static final int STREET_COLUMN_INDEX = 6;
    private static final int POSTAL_CODE_COLUMN_INDEX = 7;
    private static final int CITY_COLUMN_INDEX = 8;
    private static final int IBAN_COLUMN_INDEX = 9;

    private static final int FIRST_DATA_ROW = 2;

    public static final String FILE_PROPERTY = "node.adapter.excel.in.file";
    private static final String FALLBACK_FILE = "retirement-fund-test-data_de.xlsx";

    @Override
    public List<JobEnd> getJobEnds() {
        List<JobEnd> jobEnds = Collections.emptyList();
        try (final Workbook wb = getWorkbook()) {
            if (wb != null) {
                jobEnds = mapRows(wb.getSheetAt(0), this::mapJobEnd);
            }
        } catch (IOException e) {
            LOG.error("An exception occurred while trying to read the employment terminations", e);
        }
        return jobEnds;
    }

    @Override
    public List<JobStart> getJobStarts() {
        List<JobStart> jobStarts = Collections.emptyList();
        try (final Workbook wb = getWorkbook()) {
            if (wb != null) {
                jobStarts = mapRows(wb.getSheetAt(1), this::mapJobStart);
            }
        } catch (IOException e) {
            LOG.error("An exception occurred while trying to read the employment commencements", e);
        }
        return jobStarts;
    }

    private Workbook getWorkbook() {
        try {
            final InputStream inputStream = getFileInput();
            return WorkbookFactory.create(inputStream);
        } catch (IOException | EncryptedDocumentException | InvalidFormatException e) {
            LOG.error("An exception occurred while trying to get the workbook", e);
            return null;
        }
    }

    private InputStream getFileInput() throws FileNotFoundException {
        final String excelFilePath = System.getProperty(FILE_PROPERTY);
        return excelFilePath != null ?
                new FileInputStream(excelFilePath)
                : ExcelReader.class.getClassLoader().getResourceAsStream(FALLBACK_FILE);
    }

    private <T> List<T> mapRows(Sheet sheet, Function<Row, Optional<T>> rowMapper) {
        List<T> result = new ArrayList<>();
        int rowIndex = FIRST_DATA_ROW;
        while (sheet.getRow(rowIndex) != null) {
            rowMapper.apply(sheet.getRow(rowIndex)).ifPresent(result::add);
            rowIndex++;
        }
        return result;
    }

    private Optional<JobStart> mapJobStart(Row row) {
        JobInfo jobInfo = mapJobInfo(row);
        if (jobInfo == null) {
            return Optional.empty();
        }

        Address address = new Address();
        address.setStreet(getString(row, STREET_COLUMN_INDEX));
        address.setPostalCode(getString(row, POSTAL_CODE_COLUMN_INDEX));
        address.setCity(getString(row, CITY_COLUMN_INDEX));

        CapitalTransferInformation capititalTransferInfo = new CapitalTransferInformation();
        capititalTransferInfo.setAddress(address);
        capititalTransferInfo.setName(getString(row, NAME_COLUMN_INDEX));
        capititalTransferInfo.setAdditionalName(getString(row, ADDITIONAL_NAME_COLUMN_INDEX));
        capititalTransferInfo.setIban(getString(row, IBAN_COLUMN_INDEX));

        return Optional.of(new JobStart(null, jobInfo, capititalTransferInfo));
    }

    private Optional<JobEnd> mapJobEnd(Row row) {
        JobInfo jobInfo = mapJobInfo(row);
        if (jobInfo == null) {
            return Optional.empty();
        }
        return Optional.of(new JobEnd(null, jobInfo));
    }

    private JobInfo mapJobInfo(Row row) {
        String oasiNumber = getString(row, OASI_COLUMN_INDEX);
        if (oasiNumber.isEmpty()) {
            return null;
        }
        JobInfo jobInfo = new JobInfo();
        jobInfo.setOasiNumber(oasiNumber);
        jobInfo.setDate(getDate(row, DATE_COLUMN_INDEX));
        jobInfo.setRetirementFundUid(getString(row, RETIREMENT_FUND_UID_COLUMN_INDEX));
        jobInfo.setInternalReferenz(getString(row, REFERENCE_COLUMN_INDEX));
        return jobInfo;
    }

    private LocalDate getDate(Row row, int i) {
        Cell cell = row.getCell(i);
        if (cell != null && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }

    private String getString(Row row, int i) {
        Cell cell = row.getCell(i);
        if (cell == null) {
            return "";
        }
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                return Long.toString(Math.round(cell.getNumericCellValue()));
            case FORMULA:
                return "";
            default:
                return cell.getStringCellValue();
        }
    }
}
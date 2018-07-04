package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ExcelReader {

    private static final int OASI_COLUMN_INDEX = 0;
    private static final int DATE_COLUMN_INDEX = 1;
    private static final int RETIREMENT_FUND_UID_COLUMN_INDEX = 2;
    private static final int NAME_COLUMN_INDEX = 3;
    private static final int ADDITIONAL_NAME_COLUMN_INDEX = 4;
    private static final int STREET_COLUMN_INDEX = 5;
    private static final int POSTAL_CODE_COLUMN_INDEX = 6;
    private static final int CITY_COLUMN_INDEX = 7;
    private static final int REFERENCE_COLUMN_INDEX = 8;
    private static final int IBAN_COLUMN_INDEX = 9;
    private static final int ISR_REFERENCE_COLUMN_INDEX = 10;

    private static final int FIRST_DATA_ROW = 2;

    private final List<JobEnd> jobEnds;
    private final List<JobStart> jobStarts;


    public ExcelReader(String filename) throws IOException, InvalidFormatException {
        try (InputStream inp = getClass().getResourceAsStream(filename)) {
            Workbook wb = WorkbookFactory.create(inp);
            jobEnds = mapRows(wb.getSheetAt(0), this::mapJobEnd);
            jobStarts = mapRows(wb.getSheetAt(1), this::mapJobStart);
        }
    }

    public List<JobEnd> getJobEnds() {
        return jobEnds;
    }

    public List<JobStart> getJobStarts() {
        return jobStarts;
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
        JobStart jobStart = new JobStart();
        jobStart.setJobInfo(jobInfo);

        Address address = new Address();
        address.setStreet(getString(row, STREET_COLUMN_INDEX));
        address.setPostalCode(getString(row, POSTAL_CODE_COLUMN_INDEX));
        address.setCity(getString(row, CITY_COLUMN_INDEX));

        CapitalTransferInformation capititalTransferInfo = new CapitalTransferInformation();
        capititalTransferInfo.setAddress(address);
        capititalTransferInfo.setName(getString(row, NAME_COLUMN_INDEX));
        capititalTransferInfo.setAdditionalName(getString(row, ADDITIONAL_NAME_COLUMN_INDEX));
        capititalTransferInfo.setReference(getString(row, REFERENCE_COLUMN_INDEX));
        capititalTransferInfo.setIban(getString(row, IBAN_COLUMN_INDEX));
        capititalTransferInfo.setIsrReference(getString(row, ISR_REFERENCE_COLUMN_INDEX));
        jobStart.setCapitalTransferInfo(capititalTransferInfo);

        return Optional.of(jobStart);
    }

    private Optional<JobEnd> mapJobEnd(Row row) {
        JobInfo jobInfo = mapJobInfo(row);
        if (jobInfo == null) {
            return Optional.empty();
        }
        JobEnd jobEnd = new JobEnd();
        jobEnd.setJobInfo(jobInfo);
        return Optional.of(jobEnd);
    }

    private JobInfo mapJobInfo(Row row) {
        String oasiNumber = getString(row, OASI_COLUMN_INDEX);
        if (oasiNumber.isEmpty()) {
            return null;
        }
        JobInfo jobInfo = new JobInfo();
        jobInfo.setOasiNumber(oasiNumber);
        jobInfo.setRetirementFundUid(getString(row, RETIREMENT_FUND_UID_COLUMN_INDEX));
        jobInfo.setDate(getDate(row, DATE_COLUMN_INDEX));
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
        return cell == null ? "" : cell.getStringCellValue();
    }
}

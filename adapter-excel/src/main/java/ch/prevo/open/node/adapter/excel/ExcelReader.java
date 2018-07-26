/*============================================================================*
 * Copyright (c) 2018 - Prevo-System AG and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 3
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-3.0 WITH Classpath-exception-2.0
 *
 * Contributors:
 *     Prevo-System AG - initial API and implementation
 *===========================================================================*/
package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.CapitalTransferInformation;
import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.encrypted.model.Address;
import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import org.apache.poi.EmptyFileException;
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

import javax.validation.ConstraintViolation;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static ch.prevo.open.node.adapter.excel.ExcelConstants.COMMENCEMENTS_LABEL;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.TERMINATION_LABEL;

public class ExcelReader implements EmploymentCommencementProvider, EmploymentTerminationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReader.class);

    private static final int FIRST_DATA_ROW = 2;

    public static final String FILE_PROPERTY = "node.adapter.excel.in.file";
    private static final String FALLBACK_FILE = "retirement-fund-test-data_de.xlsx";

    @Override
    public List<EmploymentTermination> getEmploymentTerminations() {
        List<EmploymentTermination> employmentTerminations = Collections.emptyList();
        try (final Workbook wb = getWorkbook()) {
            if (wb != null) {
                employmentTerminations = mapRows(wb.getSheet(TERMINATION_LABEL), this::mapEmploymentTermination);
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
                employmentCommencements = mapRows(wb.getSheet(COMMENCEMENTS_LABEL), this::mapEmploymentCommencement);
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
                try {
                    Files.delete(path);
                } catch (IOException e1) {
                    // ignore
                }
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

    private <T> List<T> mapRows(Sheet sheet, Function<Row, Optional<T>> rowMapper) {
        List<T> result = new ArrayList<>();
        int rowIndex = FIRST_DATA_ROW;
        while (sheet.getRow(rowIndex) != null) {
            rowMapper.apply(sheet.getRow(rowIndex)).ifPresent(result::add);
            rowIndex++;
        }
        return result;
    }

    private Optional<EmploymentCommencement> mapEmploymentCommencement(Row row) {
        EmploymentInfo employmentInfo = mapEmploymentInfo(row);
        if (employmentInfo == null) {
            return Optional.empty();
        }

        Address address = new Address();
        address.setStreet(getString(row, ExcelConstants.STREET_COLUMN_INDEX));
        address.setPostalCode(getString(row, ExcelConstants.POSTAL_CODE_COLUMN_INDEX));
        address.setCity(getString(row, ExcelConstants.CITY_COLUMN_INDEX));

        CapitalTransferInformation capititalTransferInfo = new CapitalTransferInformation();
        capititalTransferInfo.setAddress(address);
        capititalTransferInfo.setName(getString(row, ExcelConstants.NAME_COLUMN_INDEX));
        capititalTransferInfo.setAdditionalName(getString(row, ExcelConstants.ADDITIONAL_NAME_COLUMN_INDEX));
        capititalTransferInfo.setIban(getString(row, ExcelConstants.IBAN_COLUMN_INDEX));

        return Optional.of(new EmploymentCommencement(employmentInfo, capititalTransferInfo));
    }

    private Optional<EmploymentTermination> mapEmploymentTermination(Row row) {
        EmploymentInfo employmentInfo = mapEmploymentInfo(row);
        if (employmentInfo == null) {
            return Optional.empty();
        }
        return Optional.of(new EmploymentTermination(employmentInfo));
    }

    private EmploymentInfo mapEmploymentInfo(Row row) {
        String oasiNumber = getString(row, ExcelConstants.OASI_COLUMN_INDEX);
        if (oasiNumber.isEmpty()) {
            return null;
        }
        EmploymentInfo employmentInfo = new EmploymentInfo();
        employmentInfo.setOasiNumber(oasiNumber);
        employmentInfo.setDate(getDate(row, ExcelConstants.DATE_COLUMN_INDEX));
        employmentInfo.setRetirementFundUid(getString(row, ExcelConstants.RETIREMENT_FUND_UID_COLUMN_INDEX));
        employmentInfo.setInternalReferenz(getString(row, ExcelConstants.REFERENCE_COLUMN_INDEX));
        return employmentInfo;
    }

    private LocalDate getDate(Row row, int i) {
        Cell cell = row.getCell(i);
        if (cell != null && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            if (date != null) {
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
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
                return cell.getStringCellValue() != null ? cell.getStringCellValue().trim() : "";
        }
    }
}

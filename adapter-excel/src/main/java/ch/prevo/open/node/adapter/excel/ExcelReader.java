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

import ch.prevo.open.data.api.Address;
import ch.prevo.open.data.api.CapitalTransferInformation;
import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.EmploymentTermination;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ExcelReader {

    private static final int FIRST_DATA_ROW = 2;

    public List<EmploymentCommencement> mapEmploymentCommencementList(Sheet sheet) {
        return mapRows(sheet, this::mapEmploymentCommencement);
    }

    public List<EmploymentTermination> mapEmploymentTerminationList(Sheet sheet) {
        return mapRows(sheet, this::mapEmploymentTermination);
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
        address.setStreet(getString(row, ExcelConstants.CommencementInput.STREET_COLUMN_INDEX));
        address.setPostalCode(getString(row, ExcelConstants.CommencementInput.POSTAL_CODE_COLUMN_INDEX));
        address.setCity(getString(row, ExcelConstants.CommencementInput.CITY_COLUMN_INDEX));

        CapitalTransferInformation capititalTransferInfo = new CapitalTransferInformation();
        capititalTransferInfo.setAddress(address);
        capititalTransferInfo.setName(getString(row, ExcelConstants.CommencementInput.NAME_COLUMN_INDEX));
        capititalTransferInfo.setAdditionalName(getString(row, ExcelConstants.CommencementInput.ADDITIONAL_NAME_COLUMN_INDEX));
        capititalTransferInfo.setIban(getString(row, ExcelConstants.CommencementInput.IBAN_COLUMN_INDEX));

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

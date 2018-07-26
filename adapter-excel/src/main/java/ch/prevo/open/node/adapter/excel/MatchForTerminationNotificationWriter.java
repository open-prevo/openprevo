package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.Address;
import ch.prevo.open.data.api.CapitalTransferInformation;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.FullMatchForTerminationNotification;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;

import static ch.prevo.open.node.adapter.excel.ExcelConstants.DATE_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.OASI_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.REFERENCE_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.RETIREMENT_FUND_UID_COLUMN_INDEX;

public class MatchForTerminationNotificationWriter extends AbstractNotificationWriter {

    private static final int COMMENCEMENT_DATA_COLUMN_INDEX = 4;
    private static final int NEW_RETIREMENT_FUND_UID_COLUMN_INDEX = 5;
    private static final int NAME_COLUMN_INDEX = 6;
    private static final int ADDITIONAL_NAME_COLUMN_INDEX = 7;
    private static final int STREET_COLUMN_NAME = 8;
    private static final int POSTAL_CODE_COLUMN_INDEX = 9;
    private static final int ADDRESS_COLUMN_INDEX = 10;
    private static final int IBAN_COLUMN_INDEX = 11;
    private static final int REFERENCE_ID_COLUMN_INDEX = 12;
    private final Sheet sheet;

    public MatchForTerminationNotificationWriter() throws IOException {
        sheet = workbook.getSheet(ExcelConstants.TERMINATION_LABEL);
    }

    public Workbook append(FullMatchForTerminationNotification notification) {
        final Row row = sheet.createRow(sheet.getLastRowNum() + 1);

        final EmploymentInfo employmentInfo = notification.getEmploymentTermination().getEmploymentInfo();
        final CapitalTransferInformation transferInformation = notification.getTransferInformation();
        final Address address = transferInformation.getAddress();
        row.createCell(OASI_COLUMN_INDEX).setCellValue(employmentInfo.getOasiNumber());
        final Cell terminationDate = row.createCell(DATE_COLUMN_INDEX);
        terminationDate.setCellValue(ExcelConstants.convert(employmentInfo.getDate()));
        terminationDate.setCellStyle(dateStyle);
        row.createCell(RETIREMENT_FUND_UID_COLUMN_INDEX).setCellValue(employmentInfo.getRetirementFundUid());
        row.createCell(REFERENCE_COLUMN_INDEX).setCellValue(employmentInfo.getInternalReferenz());

        final Cell commencementDate = row.createCell(COMMENCEMENT_DATA_COLUMN_INDEX);
        commencementDate.setCellValue(ExcelConstants.convert(notification.getCommencementDate()));
        commencementDate.setCellStyle(dateStyle);
        row.createCell(NEW_RETIREMENT_FUND_UID_COLUMN_INDEX).setCellValue(notification.getNewRetirementFundUid());
        row.createCell(NAME_COLUMN_INDEX).setCellValue(transferInformation.getName());
        row.createCell(ADDITIONAL_NAME_COLUMN_INDEX).setCellValue(transferInformation.getAdditionalName());
        row.createCell(STREET_COLUMN_NAME).setCellValue(address.getStreet());
        row.createCell(POSTAL_CODE_COLUMN_INDEX).setCellValue(address.getPostalCode());
        row.createCell(ADDRESS_COLUMN_INDEX).setCellValue(address.getCity());
        row.createCell(IBAN_COLUMN_INDEX).setCellValue(transferInformation.getIban());
        row.createCell(REFERENCE_ID_COLUMN_INDEX).setCellValue(transferInformation.getReferenceId());

        return workbook;
    }
}

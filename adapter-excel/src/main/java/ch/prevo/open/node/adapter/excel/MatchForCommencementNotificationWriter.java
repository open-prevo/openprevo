package ch.prevo.open.node.adapter.excel;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.FullMatchForCommencementNotification;

import static ch.prevo.open.node.adapter.excel.ExcelConstants.DATE_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.OASI_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.REFERENCE_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.RETIREMENT_FUND_UID_COLUMN_INDEX;

public class MatchForCommencementNotificationWriter extends AbstractNotificationWriter {

    private final Sheet sheet;

    public MatchForCommencementNotificationWriter() throws IOException {
        sheet = workbook.getSheet(ExcelConstants.COMMENCEMENTS_LABEL);
    }

    public void append(FullMatchForCommencementNotification notification) {
        final Row row = sheet.createRow(sheet.getLastRowNum() + 1);

        final EmploymentInfo employmentInfo = notification.getEmploymentCommencement().getEmploymentInfo();
        row.createCell(OASI_COLUMN_INDEX).setCellValue(employmentInfo.getOasiNumber());
        final Cell commencementDate = row.createCell(DATE_COLUMN_INDEX);
        commencementDate.setCellValue(ExcelConstants.convert(employmentInfo.getDate()));
        commencementDate.setCellStyle(dateStyle);
        row.createCell(RETIREMENT_FUND_UID_COLUMN_INDEX).setCellValue(employmentInfo.getRetirementFundUid());
        row.createCell(REFERENCE_COLUMN_INDEX).setCellValue(employmentInfo.getInternalReferenz());

        final Cell terminationDate = row.createCell(ExcelConstants.MatchForCommencementOutput.TERMINATION_DATE_COLUMN_INDEX);
        terminationDate.setCellValue(ExcelConstants.convert(notification.getTerminationDate()));
        terminationDate.setCellStyle(dateStyle);
        row.createCell(ExcelConstants.MatchForCommencementOutput.PREVIOUS_RETIREMENT_FUND_UID_COLUMN_INDEX).setCellValue(notification.getPreviousRetirementFundUid());
    }

}

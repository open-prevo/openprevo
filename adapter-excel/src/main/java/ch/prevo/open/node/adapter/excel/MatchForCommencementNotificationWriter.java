package ch.prevo.open.node.adapter.excel;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.FullMatchForCommencementNotification;

public class MatchForCommencementNotificationWriter extends AbstractNotificationWriter {

    private final Sheet sheet;

    public MatchForCommencementNotificationWriter() throws IOException {
        sheet = workbook.getSheet(COMMENCEMENTS_LABEL);
    }

    public void append(FullMatchForCommencementNotification notification) {
        final Row row = sheet.createRow(sheet.getLastRowNum() + 1);

        final EmploymentInfo employmentInfo = notification.getEmploymentCommencement().getEmploymentInfo();
        row.createCell(0).setCellValue(employmentInfo.getOasiNumber());
        final Cell commencementDate = row.createCell(1);
        commencementDate.setCellValue(convert(employmentInfo.getDate()));
        commencementDate.setCellStyle(dateStyle);
        row.createCell(2).setCellValue(employmentInfo.getRetirementFundUid());
        row.createCell(3).setCellValue(employmentInfo.getInternalReferenz());
        final Cell terminationDate = row.createCell(4);
        terminationDate.setCellValue(convert(notification.getTerminationDate()));
        terminationDate.setCellStyle(dateStyle);
        row.createCell(5).setCellValue(notification.getPreviousRetirementFundUid());
    }

}

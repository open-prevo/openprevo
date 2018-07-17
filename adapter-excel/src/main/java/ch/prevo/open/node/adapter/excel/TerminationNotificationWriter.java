package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.data.api.EmploymentInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TerminationNotificationWriter implements Closeable {

    private final String filename;
    private final Workbook workbook;
    private final Sheet sheet;
    private final CellStyle headingStyle;
    private final CellStyle dateStyle;

    public TerminationNotificationWriter(String filename) {
        this.filename = filename;
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("Eintritte");
        workbook.createSheet("Austritte");
        workbook.setActiveSheet(workbook.getSheetIndex(sheet));

        final Font font = workbook.createFont();
        font.setBold(true);
        this.headingStyle = workbook.createCellStyle();
        headingStyle.setFont(font);

        this.dateStyle = workbook.createCellStyle();
        final short shortDateFormat = workbook.getCreationHelper().createDataFormat().getFormat("d-mmm-yy");
        dateStyle.setDataFormat(shortDateFormat);

        addHeadingRow();
    }

    public void append(FullTerminationNotification notification) {
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


    @Override
    public void close() throws IOException {
        try (OutputStream fileOut = new FileOutputStream(filename)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }

    private void addHeadingRow() {
        final Row row = sheet.createRow(0);

        createHeading(row, "AHV-Nummer");
        createHeading(row, "Eintritt");
        createHeading(row, "UID der eigenen RF");
        createHeading(row, "Eigene Referenz");
        createHeading(row, "Austritt");
        createHeading(row, "UID der ehemaligen RF");
    }

    private void createHeading(Row row, String label) {
        final Cell cell = row.createCell(Math.max(0, row.getLastCellNum()));
        cell.setCellValue(label);
        cell.setCellStyle(headingStyle);
    }

    private static Date convert(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

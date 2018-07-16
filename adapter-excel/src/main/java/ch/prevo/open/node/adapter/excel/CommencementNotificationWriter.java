package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.encrypted.model.Address;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
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

public class CommencementNotificationWriter implements Closeable {

    private final String filename;
    private final Workbook workbook;
    private final Sheet sheet;
    private final CellStyle headingStyle;
    private final CellStyle dateStyle;

    public CommencementNotificationWriter(String filename) {
        this.filename = filename;
        this.workbook = new XSSFWorkbook();
        workbook.createSheet("Eintritte");
        this.sheet = workbook.createSheet("Austritte");
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

    public Workbook append(FullCommencementNotification notification) {
        final Row row = sheet.createRow(sheet.getLastRowNum() + 1);

        final JobInfo jobInfo = notification.getEmploymentTermination().getJobInfo();
        final CapitalTransferInformation transferInformation = notification.getTransferInformation();
        final Address address = transferInformation.getAddress();
        row.createCell(0).setCellValue(jobInfo.getOasiNumber());
        final Cell terminationDate = row.createCell(1);
        terminationDate.setCellValue(convert(jobInfo.getDate()));
        terminationDate.setCellStyle(dateStyle);
        row.createCell(2).setCellValue(jobInfo.getRetirementFundUid());
        row.createCell(3).setCellValue(jobInfo.getInternalReferenz());
        final Cell commencementDate = row.createCell(4);
        commencementDate.setCellValue(convert(notification.getCommencementDate()));
        commencementDate.setCellStyle(dateStyle);
        row.createCell(5).setCellValue(notification.getNewRetirementFundUid());
        row.createCell(6).setCellValue(transferInformation.getName());
        row.createCell(7).setCellValue(transferInformation.getAdditionalName());
        row.createCell(8).setCellValue(address.getStreet());
        row.createCell(9).setCellValue(address.getPostalCode());
        row.createCell(10).setCellValue(address.getCity());
        row.createCell(11).setCellValue(transferInformation.getIban());
        row.createCell(12).setCellValue(transferInformation.getReferenceId());

        return workbook;
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
        createHeading(row, "Austritt");
        createHeading(row, "UID der eigenen VE");
        createHeading(row, "Eigene Referenz");
        createHeading(row, "Eintritt");
        createHeading(row, "UID der neuen VE");
        createHeading(row, "Name der neuen VE");
        createHeading(row, "Zusatzname");
        createHeading(row, "Strasse / Postfach");
        createHeading(row, "PLZ");
        createHeading(row, "Ort");
        createHeading(row, "IBAN");
        createHeading(row, "Referenznr. der neuen VE");
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

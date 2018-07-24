package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.FullMatchForTerminationNotification;
import ch.prevo.open.encrypted.model.Address;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;

public class MatchForTerminationNotificationWriter extends AbstractNotificationWriter {

    private final Sheet sheet;

    public MatchForTerminationNotificationWriter() throws IOException {
        sheet = workbook.getSheet(TERMINATION_LABEL);
    }

    public Workbook append(FullMatchForTerminationNotification notification) {
        final Row row = sheet.createRow(sheet.getLastRowNum() + 1);

        final EmploymentInfo employmentInfo = notification.getEmploymentTermination().getEmploymentInfo();
        final CapitalTransferInformation transferInformation = notification.getTransferInformation();
        final Address address = transferInformation.getAddress();
        row.createCell(0).setCellValue(employmentInfo.getOasiNumber());
        final Cell terminationDate = row.createCell(1);
        terminationDate.setCellValue(convert(employmentInfo.getDate()));
        terminationDate.setCellStyle(dateStyle);
        row.createCell(2).setCellValue(employmentInfo.getRetirementFundUid());
        row.createCell(3).setCellValue(employmentInfo.getInternalReferenz());
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
}

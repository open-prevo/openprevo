package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

public class ExcelWriter implements MatchNotificationListener {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelWriter.class);

    private static final String FILE_PROPERTY = "node.adapter.excel.out.file";
    private static final String FALLBACK_FILE = "retirement-fund-out-data";
    private static final String FILE_NAME_FORMAT = "%1$s_%2$tY-%2$tm-%2$td-%2$tH:%2$tM:%2$tS.%2$tL.xlsx";


    @Override
    public void handleTerminationMatch(FullTerminationNotification notification) {
        Workbook wb = new XSSFWorkbook();
        writeWorkbook(wb);
    }

    @Override
    public void handleCommencementMatch(FullCommencementNotification notification) {

    }

    private void writeWorkbook(Workbook wb) {
        final String filename = String.format(FILE_NAME_FORMAT, System.getProperty(FILE_PROPERTY, FALLBACK_FILE), LocalDateTime.now());

        try (OutputStream fileOut = new FileOutputStream(filename)) {
            wb.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

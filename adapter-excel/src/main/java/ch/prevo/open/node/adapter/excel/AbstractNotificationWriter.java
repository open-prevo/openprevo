package ch.prevo.open.node.adapter.excel;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.EmptyFileException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class AbstractNotificationWriter implements Closeable {

    private static final int MAX_FILES_PER_DAY = 20;

    protected static final String COMMENCEMENTS_LABEL = "Eintritte";
    protected static final String TERMINATION_LABEL = "Austritte";

    protected final Workbook workbook;
    protected final CellStyle dateStyle;
    private final String filename;

    public static final String FILE_PROPERTY = "node.adapter.excel.out.file";
    private static final String FALLBACK_FILE = "retirement-fund-out-data";
    static final String FILE_NAME_FORMAT = "%1$s_%2$tY-%2$tm-%2$td.xlsx";
    private static final String FILE_NAME_FORMAT_PLAN_B = "%1$s_%2$tY-%2$tm-%2$td_%3$d.xlsx";


    protected AbstractNotificationWriter() throws IOException {
        final Pair<Workbook, String> pair = getWorkbook();
        this.workbook = pair.getLeft();
        this.filename =  pair.getRight();

        if (workbook.getNumberOfSheets() == 0) {
            setupNewWorkbook();
        }

        this.dateStyle = workbook.createCellStyle();
        final short shortDateFormat = workbook.getCreationHelper().createDataFormat().getFormat("d-mmm-yy");
        dateStyle.setDataFormat(shortDateFormat);
    }

    private Pair<Workbook, String> getWorkbook() throws IOException {
        final String filePrefix = System.getProperty(FILE_PROPERTY, FALLBACK_FILE);
        final LocalDate now = LocalDate.now();

        final String defaultFilename = String.format(FILE_NAME_FORMAT, filePrefix, now);
        final Optional<Workbook> defaultWorkbook = tryToReadFile(defaultFilename);
        if (defaultWorkbook.isPresent()) {
            return Pair.of(defaultWorkbook.get(), defaultFilename);
        }

        for (int i = 1; i < MAX_FILES_PER_DAY; i++) {
            final String filenamePlanB = String.format(FILE_NAME_FORMAT_PLAN_B, filePrefix, now, i);

            final Optional<Workbook> workbookPlanB = tryToReadFile(filenamePlanB);
            if (workbookPlanB.isPresent()) {
                return Pair.of(workbookPlanB.get(), filenamePlanB);
            }
        }

        throw new IOException("Unable to create file");
    }

    private Optional<Workbook> tryToReadFile(String filename) {
        if (Files.notExists(Paths.get(filename))) {
            return Optional.of(new XSSFWorkbook());
        }

        // There is no single way that works on all platforms to ensure that a file is not open.
        // Therefore we try different strategies hoping that at least one of them works.

        // try to acquire lock
        try (final RandomAccessFile file = new RandomAccessFile(filename, "rw")) {
            final FileLock fileLock = file.getChannel().tryLock();
            if (fileLock == null) {
                return Optional.empty();
            }
            fileLock.release();
        } catch (IOException e) {
            return Optional.empty();
        }

        // check for temporary Excel file
        if (Files.exists(Paths.get("~$" + filename))) {
            return Optional.empty();
        }


        try (FileInputStream inputStream = new FileInputStream(filename)) {
            // try to read the file
            return Optional.of(WorkbookFactory.create(inputStream));
        } catch (InvalidFormatException | IOException | EmptyFileException e) {
            return Optional.empty();
        }
    }

    private void setupNewWorkbook() {
            final Font font = workbook.createFont();
            font.setBold(true);
            final CellStyle headingStyle = workbook.createCellStyle();
            headingStyle.setFont(font);

            setupCommencementSheet(headingStyle);
            setupTerminationSheet(headingStyle);
    }

    private void setupCommencementSheet(CellStyle headingStyle) {
        final Sheet sheet = workbook.createSheet(COMMENCEMENTS_LABEL);
        final Row row = sheet.createRow(0);

        createHeading(row, "AHV-Nummer", headingStyle);
        createHeading(row, "Eintritt", headingStyle);
        createHeading(row, "UID der eigenen RF", headingStyle);
        createHeading(row, "Eigene Referenz", headingStyle);
        createHeading(row, "Austritt", headingStyle);
        createHeading(row, "UID der ehemaligen RF", headingStyle);
    }

    private void setupTerminationSheet(CellStyle headingStyle) {
        final Sheet sheet = workbook.createSheet(TERMINATION_LABEL);
        final Row row = sheet.createRow(0);

        createHeading(row, "AHV-Nummer", headingStyle);
        createHeading(row, "Austritt", headingStyle);
        createHeading(row, "UID der eigenen RF", headingStyle);
        createHeading(row, "Eigene Referenz", headingStyle);
        createHeading(row, "Eintritt", headingStyle);
        createHeading(row, "UID der neuen RF", headingStyle);
        createHeading(row, "Name der neuen RF", headingStyle);
        createHeading(row, "Zusatzname", headingStyle);
        createHeading(row, "Strasse / Postfach", headingStyle);
        createHeading(row, "PLZ", headingStyle);
        createHeading(row, "Ort", headingStyle);
        createHeading(row, "IBAN", headingStyle);
        createHeading(row, "Referenznr. der neuen RF", headingStyle);
    }

    private void createHeading(Row row, String label, CellStyle headingStyle) {
        final Cell cell = row.createCell(Math.max(0, row.getLastCellNum()));
        cell.setCellValue(label);
        cell.setCellStyle(headingStyle);
    }


    @Override
    public void close() throws IOException {
        try (OutputStream fileOut = new FileOutputStream(filename)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }

    protected static Date convert(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}

package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.AbstractEmployment;
import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentTermination;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.validation.ConstraintViolation;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static ch.prevo.open.node.adapter.excel.ExcelConstants.ADDITIONAL_NAME_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.CITY_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.COMMENCEMENTS_LABEL;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.DATE_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.IBAN_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.NAME_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.OASI_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.POSTAL_CODE_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.REFERENCE_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.RETIREMENT_FUND_UID_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.STREET_COLUMN_INDEX;
import static ch.prevo.open.node.adapter.excel.ExcelConstants.TERMINATION_LABEL;

public class ErrorWriter implements Closeable {

    private final Workbook workbook;
    private final String filename;
    private final CreationHelper factory;

    private final CellStyle errorStyle;
    private final CellStyle errorDateStyle;

    public ErrorWriter(Workbook workbook, String filename) {
        this.workbook = workbook;
        this.filename = filename;
        this.factory = workbook.getCreationHelper();

        this.errorStyle = workbook.createCellStyle();
        errorStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        errorStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        this.errorDateStyle = workbook.createCellStyle();
        final short shortDateFormat = workbook.getCreationHelper().createDataFormat().getFormat("d-mmm-yy");
        errorDateStyle.setDataFormat(shortDateFormat);
        errorDateStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        errorDateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    public void markTerminationErrors(Map<EmploymentTermination, Set<ConstraintViolation<EmploymentTermination>>> violationSet) {
        final Sheet sheet = workbook.getSheet(ExcelConstants.TERMINATION_LABEL);
        final Drawing<?> drawing = sheet.createDrawingPatriarch();
        violationSet.forEach(
                (termination, violations) -> {
                    final Row row = findRow(sheet, termination)
                            .orElseThrow(() -> new IllegalStateException("Violation received which does not have a matching entry in the input-table " + termination));
                    for (final ConstraintViolation<EmploymentTermination> violation : violations) {
                        final String propertyPath = violation.getPropertyPath().toString();
                        if (propertyPath.endsWith(".oasiNumber")) {
                            final Cell cell = row.getCell(OASI_COLUMN_INDEX);
                            cell.setCellStyle(errorStyle);
                            markError(drawing, cell, violation);
                        } else if (propertyPath.endsWith(".date")) {
                            final Cell cell = row.getCell(DATE_COLUMN_INDEX);
                            cell.setCellStyle(errorDateStyle);
                            markError(drawing, cell, violation);
                        } else if (propertyPath.endsWith(".retirementFundUid")) {
                            final Cell cell = row.getCell(RETIREMENT_FUND_UID_COLUMN_INDEX);
                            cell.setCellStyle(errorStyle);
                            markError(drawing, cell, violation);
                        } else if (propertyPath.endsWith(".internalReferenz")) {
                            final Cell cell = row.getCell(REFERENCE_COLUMN_INDEX);
                            cell.setCellStyle(errorStyle);
                            markError(drawing, cell, violation);
                        }
                    }
                }
        );
    }

    public void markCommencementErrors(Map<EmploymentCommencement, Set<ConstraintViolation<EmploymentCommencement>>> violationSet) {
        final Sheet sheet = workbook.getSheet(ExcelConstants.COMMENCEMENTS_LABEL);
        final Drawing<?> drawing = sheet.createDrawingPatriarch();
        violationSet.forEach(
                (commencement, violations) -> {
                    final Row row = findRow(sheet, commencement)
                            .orElseThrow(() -> new IllegalStateException("Violation received which does not have a matching entry in the input-table " + commencement));
                    for (final ConstraintViolation<EmploymentCommencement> violation : violations) {
                        final String propertyPath = violation.getPropertyPath().toString();
                        if (propertyPath.endsWith(".oasiNumber")) {
                            final Cell cell = row.getCell(OASI_COLUMN_INDEX);
                            cell.setCellStyle(errorStyle);
                            markError(drawing, cell, violation);
                        } else if (propertyPath.endsWith(".date")) {
                            final Cell cell = row.getCell(DATE_COLUMN_INDEX);
                            cell.setCellStyle(errorDateStyle);
                            markError(drawing, cell, violation);
                        } else if (propertyPath.endsWith(".retirementFundUid")) {
                            final Cell cell = row.getCell(RETIREMENT_FUND_UID_COLUMN_INDEX);
                            cell.setCellStyle(errorStyle);
                            markError(drawing, cell, violation);
                        } else if (propertyPath.endsWith(".internalReferenz")) {
                            final Cell cell = row.getCell(REFERENCE_COLUMN_INDEX);
                            cell.setCellStyle(errorStyle);
                            markError(drawing, cell, violation);
                        } else if (propertyPath.endsWith(".name")) {
                            final Cell cell = row.getCell(NAME_COLUMN_INDEX);
                            cell.setCellStyle(errorStyle);
                            markError(drawing, cell, violation);
                        } else if (propertyPath.endsWith(".additionalName")) {
                            final Cell cell = row.getCell(ADDITIONAL_NAME_COLUMN_INDEX);
                            cell.setCellStyle(errorStyle);
                            markError(drawing, cell, violation);
                        } else if (propertyPath.endsWith(".street")) {
                            final Cell cell = row.getCell(STREET_COLUMN_INDEX);
                            cell.setCellStyle(errorStyle);
                            markError(drawing, cell, violation);
                        } else if (propertyPath.endsWith(".postalCode")) {
                            final Cell cell = row.getCell(POSTAL_CODE_COLUMN_INDEX);
                            cell.setCellStyle(errorStyle);
                            markError(drawing, cell, violation);
                        } else if (propertyPath.endsWith(".city")) {
                            final Cell cell = row.getCell(CITY_COLUMN_INDEX);
                            cell.setCellStyle(errorStyle);
                            markError(drawing, cell, violation);
                        } else if (propertyPath.endsWith(".iban")) {
                            final Cell cell = row.getCell(IBAN_COLUMN_INDEX);
                            cell.setCellStyle(errorStyle);
                            markError(drawing, cell, violation);
                        }
                    }
                }
        );
    }

    private void markError(Drawing<?> drawing, Cell cell, ConstraintViolation<? extends AbstractEmployment> violation) {
        Comment comment = cell.getCellComment();
        if (comment == null) {
            final ClientAnchor anchor = factory.createClientAnchor();
            comment = drawing.createCellComment(anchor);
            cell.setCellComment(comment);
        }
        final String content = comment.getString() != null ? comment.getString().getString() + "\n" : "";
        final RichTextString str = factory.createRichTextString(content + violation.getMessage());
        comment.setString(str);
    }

    private Optional<Row> findRow(Sheet sheet, AbstractEmployment rootBean) {
        for (final Iterator it = sheet.rowIterator(); it.hasNext(); ) {
            final Row row = (Row) it.next();
            if (Objects.equals(row.getCell(OASI_COLUMN_INDEX).getStringCellValue(), rootBean.getEmploymentInfo().getOasiNumber())) {
                return Optional.of(row);
            }
        }
        return Optional.empty();
    }

    @Override
    public void close() throws IOException {
        adjustColumnWidthsInSheet(workbook.getSheet(TERMINATION_LABEL));
        adjustColumnWidthsInSheet(workbook.getSheet(COMMENCEMENTS_LABEL));

        try (OutputStream fileOut = new FileOutputStream(filename)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }

    private void adjustColumnWidthsInSheet(Sheet sheet) {
        int n = 0;
        for (final Iterator<Row> it = sheet.rowIterator(); it.hasNext(); ) {
            n = Math.max(n, it.next().getLastCellNum());
        }
        for (int i = 0; i < n; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}

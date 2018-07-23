/*******************************************************************************
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
 ******************************************************************************/
package ch.prevo.open.node.adapter.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class ExcelAssertions {

    private ExcelAssertions() {
    }

    public static void assertRow(String filename, String sheet, int rowIndex, Object... expectedValues) throws IOException, InvalidFormatException {
        try (final Workbook workbook = WorkbookFactory.create(new File(filename), null, true)) {
            final Row row = workbook.getSheet(sheet).getRow(rowIndex);
            assertThat((int) row.getLastCellNum()).isEqualTo(expectedValues.length);
            for (int i = 0; i < expectedValues.length; i++) {
                final Cell cell = row.getCell(i);
                if (expectedValues[i] instanceof LocalDate) {
                    final Date expectedDate = Date.from(((LocalDate)expectedValues[i]).atStartOfDay(ZoneId.systemDefault()).toInstant());
                    assertThat(cell.getDateCellValue()).isEqualTo(expectedDate);
                } else {
                    assertThat(cell.getStringCellValue()).isEqualTo(expectedValues[i]);
                }
            }
        }
    }

}

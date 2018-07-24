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

import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.EmploymentCommencement;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExcelReaderTest {

	@Before
	public void init(){
		System.setProperty(ExcelReader.FILE_PROPERTY, "src/test/resources/retirement-fund-test-data_de.xlsx");
	}

    @Test
    public void readEmploymentTerminations() throws Exception {
        List<EmploymentTermination> employmentTerminations = getExcelReader().getEmploymentTerminations();
        assertEquals(2, employmentTerminations.size());

        EmploymentInfo employmentInfo = employmentTerminations.get(1).getEmploymentInfo();
        assertEquals("7568152139908", employmentInfo.getOasiNumber());
        assertEquals(LocalDate.of(2018, 8, 16), employmentInfo.getDate());
        assertEquals("CHE-223.471.073", employmentInfo.getRetirementFundUid());
    }

    @Test
    public void readEmploymentCommencements() throws Exception {
        List<EmploymentCommencement> employmentCommencements = getExcelReader().getEmploymentCommencements();

        assertEquals(2, employmentCommencements.size());


        EmploymentInfo employmentInfo = employmentCommencements.get(0).getEmploymentInfo();
        assertEquals("7566374437536", employmentInfo.getOasiNumber());
        assertEquals(LocalDate.of(2018, 1, 1), employmentInfo.getDate());
        assertEquals("CHE-109.740.084", employmentInfo.getRetirementFundUid());
    }

    private ExcelReader getExcelReader() throws Exception {
        return new ExcelReader();
    }
}

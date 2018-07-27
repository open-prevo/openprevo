/*============================================================================*
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
 *===========================================================================*/
package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.Address;
import ch.prevo.open.data.api.CapitalTransferInformation;
import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.EmploymentTermination;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExcelReaderTest {

    private ExcelReader excelReader;

    @Before
    public void init() {
        System.setProperty(ExcelReader.FILE_PROPERTY, "src/test/resources/retirement-fund-test-data_de.xlsx");
        excelReader = new ExcelReader();
    }

    @Test
    public void readEmploymentTerminations() {
        // given
        EmploymentInfo employmentInfo = new EmploymentInfo("CHE-223.471.073", "our-ref-2", "7568152139908", null, LocalDate.of(2018, 8, 16));

        // when
        List<EmploymentTermination> employmentTerminations = excelReader.getEmploymentTerminations();

        // then
        assertThat(employmentTerminations).hasSize(2);
        assertThat(employmentTerminations).last().extracting("employmentInfo").containsExactly(employmentInfo);
    }

    @Test
    public void readEmploymentCommencements() {
        // given
        EmploymentInfo employmentInfo = new EmploymentInfo("CHE-109.740.084", "our-ref-55", "7566374437536", null, LocalDate.of(2018, 1, 1));

        // when
        List<EmploymentCommencement> employmentCommencements = excelReader.getEmploymentCommencements();

        // then
        assertThat(employmentCommencements).hasSize(2);
        assertThat(employmentCommencements).first().extracting("employmentInfo").containsExactly(employmentInfo);

    }

    @Test
    public void readEmploymentCommencementsWithCapitalTransferInformation() {
        // given
        EmploymentInfo employmentInfo = new EmploymentInfo("CHE-109.740.078", "our-ref-56", "7569678192446", null, LocalDate.of(2018, 8, 15));
        Address add = new Address("Str/Postfach", "PLZ", "Ort");
        CapitalTransferInformation transferInformation = new CapitalTransferInformation("Bâloise-Sammelstiftung für die ausserobligatorische berufliche Vorsorge", "Zusatzname", add, "IBAN");
        EmploymentCommencement employmentCommencement = new EmploymentCommencement(employmentInfo, transferInformation);

        // when
        List<EmploymentCommencement> employmentCommencements = excelReader.getEmploymentCommencements();

        // then
        assertThat(employmentCommencements).hasSize(2);
        assertThat(employmentCommencements).last().isEqualTo(employmentCommencement);
    }
}

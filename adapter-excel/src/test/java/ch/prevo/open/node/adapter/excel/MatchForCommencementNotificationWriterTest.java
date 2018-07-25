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

import ch.prevo.open.data.api.CapitalTransferInformation;
import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.FullMatchForCommencementNotification;
import ch.prevo.open.encrypted.model.Address;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static ch.prevo.open.node.adapter.excel.AbstractNotificationWriter.FILE_PROPERTY;
import static ch.prevo.open.node.adapter.excel.ExcelAssertions.assertRow;

public class MatchForCommencementNotificationWriterTest {

    @Test
    public void matchForCommencementNotificationShouldGenerateExcelFile() throws IOException, InvalidFormatException {
        // given
        final Address address = new Address();
        address.setStreet("St. Alban-Anlage 26");
        address.setPostalCode("4002");
        address.setCity("Basel");

        final CapitalTransferInformation transferInformation = new CapitalTransferInformation();
        transferInformation.setName("Helvetia Prisma Sammelstiftung");
        transferInformation.setAdditionalName("Helvetia Versicherungen Schweiz");
        transferInformation.setAddress(address);
        transferInformation.setIban("CH52 0483 5012 3456 7100 0");
        transferInformation.setReferenceId("756.1335.5778.23");

        final EmploymentInfo employmentInfo = new EmploymentInfo();
        employmentInfo.setRetirementFundUid("CHE-109.537.488-Helvetia-Prisma-Sammelstiftung");
        employmentInfo.setOasiNumber("756.1335.5778.23");
        employmentInfo.setDate(LocalDate.of(2018, 7, 1));
        employmentInfo.setInternalReferenz("helvetia-1");

        final EmploymentCommencement employmentCommencement = new EmploymentCommencement();
        employmentCommencement.setCapitalTransferInfo(transferInformation);
        employmentCommencement.setEmploymentInfo(employmentInfo);

        final FullMatchForCommencementNotification notification = new FullMatchForCommencementNotification();
        notification.setPreviousRetirementFundUid("CHE-109.740.084-Baloise-Sammelstiftung");
        notification.setTerminationDate(LocalDate.of(2018, 6, 30));
        notification.setEmploymentCommencement(employmentCommencement);

        final String filenamePrefix = File.createTempFile("openprevo_text", "").getAbsolutePath();
        System.setProperty(FILE_PROPERTY, filenamePrefix);
        final String filename = String.format("%1$s_%2$tY-%2$tm-%2$td.xlsx", filenamePrefix, LocalDate.now());
        new File(filename).deleteOnExit();

        // when
        final MatchForCommencementNotificationWriter writer = new MatchForCommencementNotificationWriter();
        writer.append(notification);
        writer.close();

        // then
        assertRow(filename, "Eintritte", 0,
                "AHV-Nummer",
                "Eintritt",
                "UID der eigenen RF",
                "Eigene Referenz",
                "Austritt",
                "UID der ehemaligen RF"
        );

        assertRow(filename, "Eintritte", 1,
                employmentInfo.getOasiNumber(),
                employmentInfo.getDate(),
                employmentInfo.getRetirementFundUid(),
                employmentInfo.getInternalReferenz(),
                notification.getTerminationDate(),
                notification.getPreviousRetirementFundUid()
        );
    }

}

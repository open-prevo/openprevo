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
package ch.prevo.open.node.data.provider;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import ch.prevo.open.data.api.CapitalTransferInformation;
import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.data.api.FullMatchForCommencementNotification;
import ch.prevo.open.data.api.FullMatchForTerminationNotification;
import ch.prevo.open.encrypted.model.Address;

public class MockProvider implements EmploymentTerminationProvider, EmploymentCommencementProvider, MatchNotificationListener {

    private static final Address ADDRESS = new Address("Baslerstrasse 3", "4000", "Basel");

    public static final CapitalTransferInformation CAPITAL_TRANSFER_INFO_1 = new CapitalTransferInformation("BKB_Test_Bank", null, ADDRESS, "CH53 0077 0016 02222 3334 4");


    @Override
    public List<EmploymentTermination> getEmploymentTerminations() {
        LocalDate endDate = LocalDate.of(2018, 6, 30);
        return Arrays.asList(
                new EmploymentTermination(new EmploymentInfo("CHE-109.537.488", "", "756.3412.8844.99", "", endDate)),
                new EmploymentTermination(new EmploymentInfo("CHE-109.740.084", "", "756.1335.5778.25", "", endDate)),
                new EmploymentTermination(new EmploymentInfo("CHE-109.740.078", "", "756.9534.5271.91", "", endDate)));
    }

    @Override
    public List<EmploymentCommencement> getEmploymentCommencements() {
        LocalDate startDate = LocalDate.of(2018, 7, 1);

        return Arrays.asList(
                new EmploymentCommencement(new EmploymentInfo("CHE-109.740.084", "", "756.1234.5678.97", "", startDate),
                        CAPITAL_TRANSFER_INFO_1),
                new EmploymentCommencement(new EmploymentInfo("CHE-109.740.078", "", "756.5678.1234.11", "", startDate),
                        new CapitalTransferInformation("BKB_Test_Bank", null, ADDRESS, "CH53 0077 0016 02222 3334 4")),
                new EmploymentCommencement(new EmploymentInfo("CHE-109.537.488", "", "756.1298.6578.93", "", startDate),
                        new CapitalTransferInformation("BKB_Test_Bank", null, ADDRESS, "CH53 0077 0016 02222 3334 4")));
    }

    @Override
    public void handleMatchForCommencementNotification(FullMatchForCommencementNotification notification) {

    }

    @Override
    public void handleMatchForTerminationNotification(FullMatchForTerminationNotification notification) {

    }
}

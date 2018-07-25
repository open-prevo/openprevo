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
package ch.prevo.open.data.api;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDate;

public class FullMatchForTerminationNotification {

    private String newRetirementFundUid;
    private LocalDate commencementDate;
    private CapitalTransferInformation transferInformation;

    private EmploymentTermination employmentTermination;

    public String getNewRetirementFundUid() {
        return newRetirementFundUid;
    }

    public void setNewRetirementFundUid(String newRetirementFundUid) {
        this.newRetirementFundUid = newRetirementFundUid;
    }

    public LocalDate getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public CapitalTransferInformation getTransferInformation() {
        return transferInformation;
    }

    public void setTransferInformation(CapitalTransferInformation transferInformation) {
        this.transferInformation = transferInformation;
    }

    public EmploymentTermination getEmploymentTermination() {
        return employmentTermination;
    }

    public void setEmploymentTermination(EmploymentTermination employmentTermination) {
        this.employmentTermination = employmentTermination;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("newRetirementFundUid", newRetirementFundUid)
                .append("commencementDate", commencementDate)
                .append("transferInformation", transferInformation)
                .append("employmentTermination", employmentTermination)
                .toString();
    }
}

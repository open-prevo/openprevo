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
package ch.prevo.open.data.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDate;

public class FullTerminationNotification {

    private String previousRetirementFundUid;
    private LocalDate terminationDate;

    private EmploymentCommencement employmentCommencement;

    public String getPreviousRetirementFundUid() {
        return previousRetirementFundUid;
    }

    public void setPreviousRetirementFundUid(String previousRetirementFundUid) {
        this.previousRetirementFundUid = previousRetirementFundUid;
    }

    public LocalDate getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }

    public EmploymentCommencement getEmploymentCommencement() {
        return employmentCommencement;
    }

    public void setEmploymentCommencement(EmploymentCommencement employmentCommencement) {
        this.employmentCommencement = employmentCommencement;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("previousRetirementFundUid", previousRetirementFundUid)
                .append("terminationDate", terminationDate)
                .append("employmentCommencement", employmentCommencement)
                .toString();
    }
}

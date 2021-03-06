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

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ch.prevo.open.data.api.validation.OASI;

public class EmploymentInfo {

    @NotNull
    @Pattern(regexp = "CHE-([0-9]{3}\\.){2}[0-9]{3}")
    private String retirementFundUid;
    private String internalReferenz;
    @NotNull
    @OASI
    private String oasiNumber;
    private String internalPersonId;
    @NotNull
    private LocalDate date;

    public EmploymentInfo() {
    }

    public EmploymentInfo(String retirementFundUid, String oasiNumber) {
        this.retirementFundUid = retirementFundUid;
        this.oasiNumber = oasiNumber;
    }

    public EmploymentInfo(String retirementFundUid, String internalReferenz, String oasiNumber, String internalPersonId, LocalDate date) {
        this.retirementFundUid = retirementFundUid;
        this.internalReferenz = internalReferenz;
        this.oasiNumber = oasiNumber;
        this.internalPersonId = internalPersonId;
        this.date = date;
    }

    public String getRetirementFundUid() {
        return retirementFundUid;
    }

    public void setRetirementFundUid(String retirementFundUid) {
        this.retirementFundUid = retirementFundUid;
    }

    public String getInternalReferenz() {
        return internalReferenz;
    }

    public void setInternalReferenz(String internalReferenz) {
        this.internalReferenz = internalReferenz;
    }

    public String getOasiNumber() {
        return oasiNumber;
    }

    public void setOasiNumber(String oasiNumber) {
        this.oasiNumber = oasiNumber;
    }

    public String getInternalPersonId() {
        return internalPersonId;
    }

    public void setInternalPersonId(String internalPersonId) {
        this.internalPersonId = internalPersonId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EmploymentInfo employmentInfo = (EmploymentInfo) o;

        return new EqualsBuilder()
                .append(retirementFundUid, employmentInfo.retirementFundUid)
                .append(internalReferenz, employmentInfo.internalReferenz)
                .append(oasiNumber, employmentInfo.oasiNumber)
                .append(internalPersonId, employmentInfo.internalPersonId)
                .append(date, employmentInfo.date)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(retirementFundUid)
                .append(internalReferenz)
                .append(oasiNumber)
                .append(internalPersonId)
                .append(date)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("retirementFundUid", retirementFundUid)
                .append("internalReferenz", internalReferenz)
                .append("oasiNumber", oasiNumber)
                .append("internalPersonId", internalPersonId)
                .append("date", date)
                .toString();
    }
}

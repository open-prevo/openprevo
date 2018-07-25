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
package org.example.prevo.open.adapter.dto;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
public class EmploymentInfoDTO {

    @Id
    private long id;
    private String retirementFundUid;
    private String internalReferenz;
    private String oasiNumber;
    private String internalPersonId;
    private LocalDate date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("retirementFundUid", retirementFundUid)
                .append("internalReferenz", internalReferenz)
                .append("oasiNumber", oasiNumber)
                .append("internalPersonId", internalPersonId)
                .append("date", date)
                .toString();
    }
}

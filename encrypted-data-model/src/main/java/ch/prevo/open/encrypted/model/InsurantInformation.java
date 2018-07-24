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
package ch.prevo.open.encrypted.model;

import java.time.LocalDate;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Encrypted information to exchange with OpenPrevo Hub.
 */
public class InsurantInformation implements Comparable<InsurantInformation> {

    private String encryptedOasiNumber;
    private String retirementFundUid;
    private LocalDate date;

    public InsurantInformation() {
    }

    public InsurantInformation(String encryptedOasiNumber, String retirementFundUid) {
        this.encryptedOasiNumber = encryptedOasiNumber;
        this.retirementFundUid = retirementFundUid;
    }

    public InsurantInformation(String encryptedOasiNumber, String retirementFundUid, LocalDate date) {
        this.encryptedOasiNumber = encryptedOasiNumber;
        this.retirementFundUid = retirementFundUid;
        this.date = date;
    }

    public String getEncryptedOasiNumber() {
        return encryptedOasiNumber;
    }

    public void setEncryptedOasiNumber(String encryptedOasiNumber) {
        this.encryptedOasiNumber = encryptedOasiNumber;
    }

    public String getRetirementFundUid() {
        return retirementFundUid;
    }

    public void setRetirementFundUid(String retirementFundUid) {
        this.retirementFundUid = retirementFundUid;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InsurantInformation that = (InsurantInformation) o;

        if (encryptedOasiNumber != null ? !encryptedOasiNumber.equals(that.encryptedOasiNumber) : that.encryptedOasiNumber != null) {
            return false;
        }
        if (retirementFundUid != null ? !retirementFundUid.equals(that.retirementFundUid) : that.retirementFundUid != null) {
            return false;
        }
        return date != null ? date.equals(that.date) : that.date == null;
    }

    @Override
    public int hashCode() {
        int result = encryptedOasiNumber != null ? encryptedOasiNumber.hashCode() : 0;
        result = 31 * result + (retirementFundUid != null ? retirementFundUid.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(InsurantInformation o) {
        int oasiComparisonResult = ObjectUtils.compare(encryptedOasiNumber, o.encryptedOasiNumber);
        if (oasiComparisonResult != 0) {
            return oasiComparisonResult;
        }
        int uidComparisonResult = ObjectUtils.compare(retirementFundUid, o.retirementFundUid);
        if (uidComparisonResult != 0) {
            return uidComparisonResult;
        }
        return ObjectUtils.compare(date, o.date);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("encryptedOasiNumber", encryptedOasiNumber)
                .append("retirementFundUid", retirementFundUid)
                .append("date", date)
                .toString();
    }
}

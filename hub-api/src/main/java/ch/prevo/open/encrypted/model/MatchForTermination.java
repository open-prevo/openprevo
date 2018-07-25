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
package ch.prevo.open.encrypted.model;

import java.time.LocalDate;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Matching notification sent to an OpenPrevo Node.
 */
public class MatchForTermination extends MatchNotification {

    private EncryptedData transferInformation;


    public MatchForTermination() {
    }

    public MatchForTermination(String encryptedOasiNumber, String previousRetirementFundUid, String newRetirementFundUid, LocalDate commencementDate, LocalDate terminationDate, EncryptedData transferInformation) {
        super(encryptedOasiNumber, previousRetirementFundUid, newRetirementFundUid, commencementDate, terminationDate);
        this.transferInformation = transferInformation;
    }

    public EncryptedData getTransferInformation() {
        return transferInformation;
    }

    public void setTransferInformation(EncryptedData transferInformation) {
        this.transferInformation = transferInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MatchForTermination that = (MatchForTermination) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(transferInformation, that.transferInformation)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(transferInformation)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("transferInformation", transferInformation)
                .toString();
    }
}

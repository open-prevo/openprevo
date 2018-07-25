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
package ch.prevo.open.node.data.provider.dummy;

import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.FullMatchForCommencementNotification;
import ch.prevo.open.data.api.FullMatchForTerminationNotification;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;

import java.io.PrintWriter;

class NotificationWriter {

    void write(PrintWriter writer, FullMatchForCommencementNotification notification) {

        writer.println("\n\n---------------------------------------");
        writer.println("Match found for employment commencement: ");

        final EmploymentInfo employmentInfo = notification.getEmploymentCommencement().getEmploymentInfo();
        if (employmentInfo != null) {
            writer.println("\nMy data (employment commencement)");
            writer.println("OASI number:                  " + employmentInfo.getOasiNumber());
            writer.println("Employment commencement date: " + employmentInfo.getDate());
            writer.println("Internal Reference:           " + employmentInfo.getInternalReferenz());
            writer.println("Retirement fund:              " + employmentInfo.getRetirementFundUid());
        }

        writer.println("\nOther data (employment termination)");
        writer.println("Previous retirement fund:     " + notification.getPreviousRetirementFundUid());
        writer.println("Employment termination date:  " + notification.getTerminationDate());

        writer.println("\nThe previous retirement fund will receive a notification with capital transfer details.");

        writer.flush();
    }

    void write(PrintWriter writer, FullMatchForTerminationNotification notification) {
        writer.println("\n\n---------------------------------------");
        writer.println("Match found for employment termination");

        final EmploymentInfo employmentInfo = notification.getEmploymentTermination().getEmploymentInfo();
        if (employmentInfo != null) {
            writer.println("\nMy data (employment termination)");
            writer.println("OASI number:                 " + employmentInfo.getOasiNumber());
            writer.println("Employment termination date: " + employmentInfo.getDate());
            writer.println("Internal Reference:          " + employmentInfo.getInternalReferenz());
            writer.println("Retirement fund:             " + employmentInfo.getRetirementFundUid());
        }

        writer.println("\nOther data (employment commencement)");
        writer.println("New retirement fund:           " + notification.getNewRetirementFundUid());
        writer.println("Employment commencement date:  " + notification.getCommencementDate());
        writer.println("Capital transfer information");
        final CapitalTransferInformation transferInformation = notification.getTransferInformation();
        if (transferInformation != null) {
            writer.println("Name:                 " + transferInformation.getName());
            writer.println("Additional name:      " + transferInformation.getAdditionalName());
            writer.println("IBAN:                 " + transferInformation.getIban());
            if (transferInformation.getAddress() != null) {
                writer.println("Street:               " + transferInformation.getAddress().getStreet());
                writer.println("Postal code / city:   " + transferInformation.getAddress().getPostalCode() + " "
                        + transferInformation.getAddress().getCity());
            }
        }

        writer.flush();
    }
}

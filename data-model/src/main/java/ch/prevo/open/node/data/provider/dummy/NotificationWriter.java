package ch.prevo.open.node.data.provider.dummy;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;

import java.io.PrintWriter;

class NotificationWriter {

    void write(PrintWriter writer, FullTerminationNotification notification) {

        writer.println("\n\n---------------------------------------");
        writer.println("Match found for employment commencement: ");

        final EmploymentInfo jobInfo = notification.getEmploymentCommencement().getEmploymentInfo();
        if (jobInfo != null) {
            writer.println("\nMy data (employment commencement)");
            writer.println("OASI number:                  " + jobInfo.getOasiNumber());
            writer.println("Employment commencement date: " + jobInfo.getDate());
            writer.println("Internal Reference:           " + jobInfo.getInternalReferenz());
            writer.println("Retirement fund:              " + jobInfo.getRetirementFundUid());
        }

        writer.println("\nOther data (employment termination)");
        writer.println("Previous retirement fund:     " + notification.getPreviousRetirementFundUid());
        writer.println("Employment termination date:  " + notification.getTerminationDate());

        writer.println("\nThe previous retirement fund will receive a notification with capital transfer details.");

        writer.flush();
    }

    void write(PrintWriter writer, FullCommencementNotification notification) {
        writer.println("\n\n---------------------------------------");
        writer.println("Match found for employment termination");

        final EmploymentInfo jobInfo = notification.getEmploymentTermination().getEmploymentInfo();
        if (jobInfo != null) {
            writer.println("\nMy data (employment termination)");
            writer.println("OASI number:                 " + jobInfo.getOasiNumber());
            writer.println("Employment termination date: " + jobInfo.getDate());
            writer.println("Internal Reference:          " + jobInfo.getInternalReferenz());
            writer.println("Retirement fund:             " + jobInfo.getRetirementFundUid());
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

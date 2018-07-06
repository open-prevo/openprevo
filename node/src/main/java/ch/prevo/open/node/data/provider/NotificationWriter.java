package ch.prevo.open.node.data.provider;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;

import java.io.PrintWriter;

class NotificationWriter {

    void write(PrintWriter writer, TerminationMatchNotification matchNotification) {
        writer.println("---------------------------------------");
        writer.println("Match found for employment commencement: " + matchNotification.getReferenceId());
        writer.println();
        writer.println("OASI number:                  " + matchNotification.getEncryptedOasiNumber());
        writer.println("Previous retirement fund:     " + matchNotification.getPreviousRetirementFundUid());
        writer.println("Employment commencement date: " + matchNotification.getEntryDate());
        writer.println();
        writer.println("The previous retirement fund will receive a notification with capital transfer details.");
        writer.flush();
    }

    void write(PrintWriter writer, CommencementMatchNotification matchNotification) {
        writer.println("---------------------------------------");
        writer.println("Match found for employment termination");
        writer.println();
        writer.println("OASI number:                 " + matchNotification.getEncryptedOasiNumber());
        writer.println("Employment termination date: " + matchNotification.getExitDate());
        writer.println("New retirmeent fund:         " + matchNotification.getNewRetirementFundUid());
        writer.println();
        writer.println("Capital transfer information");
        final CapitalTransferInformation transferInformation = matchNotification.getTransferInformation();
        writer.println("Name:                 " + transferInformation.getName());
        writer.println("Additional name:      " + transferInformation.getAdditionalName());
        writer.println("IBAN:                 " + transferInformation.getIban());
        if (transferInformation.getAddress() != null) {
            writer.println("Street:               " + transferInformation.getAddress().getStreet());
            writer.println("Postal code / city:   " + transferInformation.getAddress().getPostalCode() + " "
                    + transferInformation.getAddress().getCity());
        }
        writer.flush();
    }
}

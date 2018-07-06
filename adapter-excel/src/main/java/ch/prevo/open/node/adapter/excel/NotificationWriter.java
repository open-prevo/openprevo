package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;

import java.io.IOException;
import java.io.PrintWriter;

class NotificationWriter {


    void write(PrintWriter writer, CommencementMatchNotification matchNotification, JobInfo jobInfo) throws IOException {
        writer.println("Match found for employment commencement: " + jobInfo.getInternalPersonId());
        writer.println();
        writer.println("OASI number:                  " + jobInfo.getOasiNumber());
        writer.println("Previous retirmeent fund:     " + matchNotification.getNewRetirementFundUid());
        writer.println("Employment commencement date: " + jobInfo.getDate());
        writer.println();
        writer.println("The previous retirmenemt fund will receive a notification with capital transfer details.");
    }

    void write(PrintWriter writer, CommencementMatchNotification matchNotification, CapitalTransferInformation capitalTransferInfo, JobInfo jobInfo) throws IOException {
        writer.println("Match found for employment termination: " + jobInfo.getInternalPersonId());
        writer.println();
        writer.println("OASI number:                 " + jobInfo.getOasiNumber());
        writer.println("Employment termination date: " + jobInfo.getDate());
        writer.println("New retirmeent fund:         " + matchNotification.getNewRetirementFundUid());
        writer.println();
        writer.println("Capital transfer information");
        writer.println("Name:                 " + capitalTransferInfo.getName());
        writer.println("Additional name:      " + capitalTransferInfo.getAdditionalName());
        writer.println("Street:               " + capitalTransferInfo.getAddress().getStreet());
        writer.println("Postal code / city:   " + capitalTransferInfo.getAddress().getPostalCode() + " " + capitalTransferInfo.getAddress().getCity());
        writer.println("IBAN:                 " + capitalTransferInfo.getIban());
    }
}

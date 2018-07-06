package ch.prevo.open.node.data.provider;

import ch.prevo.open.data.api.JobStart;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;

import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import javax.inject.Inject;

@Service
public class NotificationListener implements MatchNotificationListener {

    private final PrintWriter writer = new PrintWriter(System.out);
    private final NotificationWriter notificationWriter = new NotificationWriter();

    private final JobStartProvider jobStartProvider;

    @Inject
    public NotificationListener(JobStartProvider jobStartProvider) {
        this.jobStartProvider = jobStartProvider;
    }

    @Override
    public CapitalTransferInformation handleTerminationMatch(TerminationMatchNotification notification) {
        notificationWriter.write(writer, notification);

        return jobStartProvider.getJobStarts().stream()
                .filter(j -> j.getJobInfo().getOasiNumber().equals(notification.getEncryptedOasiNumber())).findAny()
                .map(JobStart::getCapitalTransferInfo).orElse(null);
    }

    @Override
    public void handleCommencementMatch(CommencementMatchNotification notification) {
        notificationWriter.write(writer, notification);
    }
}

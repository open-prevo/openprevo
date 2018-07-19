package ch.prevo.open.node.data.provider;

import ch.prevo.open.data.api.*;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class MockProvider implements EmploymentTerminationProvider, EmploymentCommencementProvider, MatchNotificationListener {

    public static final CapitalTransferInformation CAPITAL_TRANSFER_INFO_1 = new CapitalTransferInformation("BKB_Test_Bank", "CH53 0077 0016 02222 3334 4");

    @Override
    public List<EmploymentTermination> getEmploymentTerminations() {
        int i = 0;
        LocalDate endDate = LocalDate.of(2018, 6, 30);
        return Arrays.asList(
                new EmploymentTermination(Integer.toString(++i), new EmploymentInfo("CHE-109.537.488", "", "756.3412.8844.97", "", endDate)),
                new EmploymentTermination(Integer.toString(++i), new EmploymentInfo("CHE-109.740.084", "", "756.1335.5778.23", "", endDate)),
                new EmploymentTermination(Integer.toString(++i), new EmploymentInfo("CHE-109.740.078", "", "756.9534.5271.94", "", endDate)));
    }

    @Override
    public List<EmploymentCommencement> getEmploymentCommencements() {
        int i = 0;
        LocalDate startDate = LocalDate.of(2018, 7, 1);

        return Arrays.asList(
                new EmploymentCommencement(Integer.toString(++i),
                        new EmploymentInfo("CHE-109.740.084", "", "756.1234.5678.97", "", startDate),
                        CAPITAL_TRANSFER_INFO_1),
                new EmploymentCommencement(Integer.toString(++i),
                        new EmploymentInfo("CHE-109.740.078", "", "756.5678.1234.17", "", startDate),
                        new CapitalTransferInformation("BKB_Test_Bank", "CH53 0077 0016 02222 3334 4")),
                new EmploymentCommencement(Integer.toString(++i),
                        new EmploymentInfo("CHE-109.537.488", "", "756.1298.6578.97", "", startDate),
                        new CapitalTransferInformation("BKB_Test_Bank", "CH53 0077 0016 02222 3334 4")));
    }

    @Override
    public void handleTerminationMatch(FullTerminationNotification notification) {

    }

    @Override
    public void handleCommencementMatch(FullCommencementNotification notification) {

    }
}

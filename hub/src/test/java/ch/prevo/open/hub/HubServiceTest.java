package ch.prevo.open.hub;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.hub.match.Match;
import ch.prevo.open.hub.nodes.NodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HubServiceTest {

    @MockBean
    private NodeService nodeService;

    @Inject
    private HubService hubService;

    @SuppressWarnings("unchecked")
    @Test
    public void matchAndNotify() {
        InsurantInformation entry = new InsurantInformation("123", "1");
        InsurantInformation exit = new InsurantInformation("123", "2");
        when(nodeService.getCurrentCommencements()).thenReturn(singleton(entry));
        when(nodeService.getCurrentTerminations()).thenReturn(singleton(exit));
        ArgumentCaptor<List<Match>> nodeServiceArgumentCaptor = ArgumentCaptor.forClass(List.class);

        hubService.matchAndNotify();

        verify(nodeService).notifyMatches(nodeServiceArgumentCaptor.capture());
        List<Match> notifiedMatches = nodeServiceArgumentCaptor.getValue();
        assertEquals(1, notifiedMatches.size());
        assertEquals(exit.getRetirementFundUid(), notifiedMatches.get(0).getPreviousRetirementFundUid());
        assertEquals(entry.getRetirementFundUid(), notifiedMatches.get(0).getNewRetirementFundUid());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void matchAndNotifySeveralTerminationsForSingleCommencement() {
        // given
        final LocalDate terminationDate = LocalDate.of(2018, 6, 30);
        final Set<InsurantInformation> terminations = new HashSet<>(Arrays.asList(
                new InsurantInformation("OASI_1", "UID_1", terminationDate),
                new InsurantInformation("OASI_1", "UID_2", terminationDate)
        ));
        final LocalDate commencementDate = LocalDate.of(2018, 7, 1);
        final Set<InsurantInformation> commencements = Collections.singleton(
                new InsurantInformation("OASI_1", "UID_3", commencementDate)
        );
        when(nodeService.getCurrentCommencements()).thenReturn(commencements);
        when(nodeService.getCurrentTerminations()).thenReturn(terminations);

        // when
        hubService.matchAndNotify();

        // then
        final ArgumentCaptor<List<Match>> nodeServiceArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(nodeService).notifyMatches(nodeServiceArgumentCaptor.capture());
        final List<Match> notifiedMatches = nodeServiceArgumentCaptor.getValue();
        assertThat(notifiedMatches).containsExactlyInAnyOrder(
                new Match("OASI_1", "UID_1", "UID_3", commencementDate, terminationDate),
                new Match("OASI_1", "UID_2", "UID_3", commencementDate, terminationDate)
        );
    }
}

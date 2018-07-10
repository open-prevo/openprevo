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
import java.util.List;

import static java.util.Collections.singleton;
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

    @Test
    public void matchAndNotify() throws Exception {
        InsurantInformation entry = new InsurantInformation("123", "1");
        InsurantInformation exit = new InsurantInformation("123", "2");
        when(nodeService.getCurrentEntries()).thenReturn(singleton(entry));
        when(nodeService.getCurrentExits()).thenReturn(singleton(exit));
        ArgumentCaptor<List<Match>> nodeServiceArgumentCaptor = ArgumentCaptor.forClass(List.class);

        hubService.matchAndNotify();

        verify(nodeService).notifyMatches(nodeServiceArgumentCaptor.capture());
        List<Match> notifiedMatches = nodeServiceArgumentCaptor.getValue();
        assertEquals(1, notifiedMatches.size());
        assertEquals(exit.getRetirementFundUid(), notifiedMatches.get(0).getPreviousRetirementFundUid());
        assertEquals(entry.getRetirementFundUid(), notifiedMatches.get(0).getNewRetirementFundUid());
    }

}
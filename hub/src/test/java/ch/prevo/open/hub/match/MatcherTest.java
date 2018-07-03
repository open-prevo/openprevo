package ch.prevo.open.hub.match;

import ch.prevo.open.encrypted.model.InsurantInformation;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.junit.Assert.*;

public class MatcherTest {

    @Test
    public void findMatches() throws Exception {
        Set<InsurantInformation> exits = new HashSet<>();
        exits.add(new InsurantInformation("oasi1", "uid1"));
        Set<InsurantInformation> entries = new HashSet<>();
        entries.add(new InsurantInformation("oasi1", "uid2"));
        entries.add(new InsurantInformation("oasi2", "uid3"));

        List<Match> matches = new MatcherService().findMatches(exits, entries);

        assertEquals(1, matches.size());
        Match match = matches.get(0);
        assertEquals("oasi1", match.getEncryptedOasiNumber());
        assertNotEquals(match.getPreviousRetirementFundUid(), match.getNewRetirementFundUid());
    }

    @Test
    public void findMatchesEmptyInput() throws Exception {
        assertTrue(new MatcherService().findMatches(emptySet(), emptySet()).isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void findMatchesWithDuplicates() throws Exception {
        Set<InsurantInformation> exits = new HashSet<>();
        exits.add(new InsurantInformation("oasi1", "uid1"));
        Set<InsurantInformation> entries = new HashSet<>();
        entries.add(new InsurantInformation("oasi1", "uid2"));
        entries.add(new InsurantInformation("oasi1", "uid3"));

        new MatcherService().findMatches(exits, entries);
    }
}
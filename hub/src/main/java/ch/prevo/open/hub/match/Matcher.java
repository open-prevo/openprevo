package ch.prevo.open.hub.match;

import ch.prevo.open.encrypted.model.InsurantInformation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class Matcher {

    public List<Match> findMatches(Set<InsurantInformation> retirementFundExits, Set<InsurantInformation> retirementFundEntries) {
        List<Match> matches = new ArrayList<>();
        for (InsurantInformation exit : retirementFundExits) {
            InsurantInformation matchingEntry = findMatchingEntry(retirementFundEntries, exit);
            if (matchingEntry != null) {
                matches.add(new Match(exit.getEncryptedOasiNumber(), exit.getRetirementFundUid(), matchingEntry.getRetirementFundUid()));
            }
        }
        return matches;
    }

    private InsurantInformation findMatchingEntry(Set<InsurantInformation> retirementFundEntries, InsurantInformation exit) {
        Set<InsurantInformation> matchingEntries = retirementFundEntries.stream()
                .filter(entry -> entry.getEncryptedOasiNumber().equals(exit.getEncryptedOasiNumber()))
                .collect(Collectors.toSet());

        if (matchingEntries.isEmpty()) {
            return null;
        }
        if (matchingEntries.size() > 1) {
            throw new RuntimeException("Cannot handle multiple entries for a single exit");
        }
        return matchingEntries.iterator().next();
    }

}

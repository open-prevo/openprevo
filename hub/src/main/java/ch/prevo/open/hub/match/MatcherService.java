package ch.prevo.open.hub.match;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ch.prevo.open.encrypted.model.InsurantInformation;

@Service
public class MatcherService {

    public List<Match> findMatches(Set<InsurantInformation> retirementFundTerminations, Set<InsurantInformation> retirementFundCommencements) {
        List<Match> matches = new ArrayList<>();
        for (InsurantInformation termination : retirementFundTerminations) {
            InsurantInformation matchingEntry = findMatchingEntry(retirementFundCommencements, termination);
            if (matchingEntry != null) {
                matches.add(new Match(termination.getEncryptedOasiNumber(), termination.getRetirementFundUid(), matchingEntry.getRetirementFundUid(), matchingEntry.getDate(), termination.getDate()));
            }
        }
        return matches;
    }

    private InsurantInformation findMatchingEntry(Set<InsurantInformation> retirementFundEntries, InsurantInformation exit) {
        Set<InsurantInformation> matchingEntries = retirementFundEntries.stream()
                .filter(entry -> isMatching(entry, exit))
                .collect(Collectors.toSet());

        if (matchingEntries.isEmpty()) {
            return null;
        }
        if (matchingEntries.size() > 1) {
            throw new RuntimeException("Cannot handle multiple entries for a single exit");
        }
        return matchingEntries.iterator().next();
    }

    private boolean isMatching(InsurantInformation entry, InsurantInformation exit) {
        return entry.getEncryptedOasiNumber().equals(exit.getEncryptedOasiNumber())
                && !isSameFundWithEntryBeforeExit(entry, exit);
    }

    private boolean isSameFundWithEntryBeforeExit(InsurantInformation entry, InsurantInformation exit) {
        return entry.getRetirementFundUid().equals(exit.getRetirementFundUid())
                && entry.getDate().isBefore(exit.getDate());
    }
}

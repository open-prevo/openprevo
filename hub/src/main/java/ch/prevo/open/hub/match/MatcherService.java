/*******************************************************************************
 * Copyright (c) 2018 - Prevo-System AG and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 3
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-3.0 WITH Classpath-exception-2.0
 * 
 * Contributors:
 *     Prevo-System AG - initial API and implementation
 ******************************************************************************/
package ch.prevo.open.hub.match;

import ch.prevo.open.encrypted.model.InsurantInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatcherService {

    private static final Logger LOG = LoggerFactory.getLogger(MatcherService.class);

    public List<Match> findMatches(Set<InsurantInformation> retirementFundTerminations, Set<InsurantInformation> retirementFundCommencements) {
        final List<Match> matches = new ArrayList<>();
        for (InsurantInformation termination : retirementFundTerminations) {
            final Optional<InsurantInformation> matchingEntry = findMatchingEntry(retirementFundCommencements, termination);
            matchingEntry.ifPresent(commencement -> matches.add(
                    new Match(
                            termination.getEncryptedOasiNumber(),
                            termination.getRetirementFundUid(),
                            commencement.getRetirementFundUid(),
                            commencement.getDate(),
                            termination.getDate()
                    )
            ));
        }
        return matches;
    }

    private Optional<InsurantInformation> findMatchingEntry(Set<InsurantInformation> retirementFundEntries, InsurantInformation termination) {
        final List<InsurantInformation> result = retirementFundEntries.stream().filter(commencement -> isMatching(commencement, termination)).collect(Collectors.toList());
        if (result.size() > 1) {
            LOG.warn("Found more than one commencement for termination " + termination + ". Commencements: " + result);
            return Optional.empty();
        }
        return result.stream().findAny();
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

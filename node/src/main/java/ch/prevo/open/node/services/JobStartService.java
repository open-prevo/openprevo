package ch.prevo.open.node.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import ch.prevo.open.encrypted.model.InsurantInformation;

/**
 * Dummy mock service implementation to retrieve encrypted information about an insurant.
 */
@Service
public class JobStartService {

    public Set<InsurantInformation> getAllJobStartData() {
        return new HashSet<>(Arrays.asList(new InsurantInformation("756.1234.5678.97", "CHE-109.740.084"),
                new InsurantInformation("756.5678.1234.17", "CHE-109.740.078"),
                new InsurantInformation("756.1298.6578.97", "CHE-109.537.488")));
    }
}

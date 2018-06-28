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
public class JobEndService {

    public Set<InsurantInformation> getAllJobEndData() {
        return new HashSet<>(Arrays.asList(new InsurantInformation("756.3412.8844.97", "CHE-109.537.488"),
                new InsurantInformation("756.1335.5778.23", "CHE-109.740.084"),
                new InsurantInformation("756.9534.5271.94", "CHE-109.740.078")));
    }
}

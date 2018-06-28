package ch.prevo.open.node.services;

import java.util.Set;
import javax.inject.Inject;

import org.springframework.stereotype.Service;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.node.adapter.JsonAdapter;

/**
 * Dummy mock service implementation to retrieve encrypted information about an insurant.
 */
@Service
public class JobEndService {

    @Inject
    private JsonAdapter jsonAdapter;

    public Set<InsurantInformation> getAllJobEndData() {
        return jsonAdapter.getJobEndInformation();
    }
}

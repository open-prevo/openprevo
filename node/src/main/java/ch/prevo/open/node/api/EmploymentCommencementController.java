package ch.prevo.open.node.api;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.node.services.EmploymentCommencementService;

@RestController
class EmploymentCommencementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmploymentCommencementController.class);

    private EmploymentCommencementService employmentCommencementService;

    @Inject
    public EmploymentCommencementController(EmploymentCommencementService employmentCommencementService) {
        this.employmentCommencementService = employmentCommencementService;
    }

    @RequestMapping("/commencement-of-employment")
    public ResponseEntity<SortedSet<InsurantInformation>> getAllEmploymentCommencementData() {
        try {
            Set<InsurantInformation> employmentCommencementData = this.employmentCommencementService.getAllEmploymentCommencementData();
            return ResponseEntity.ok(new TreeSet<>(employmentCommencementData));
        } catch (Exception e) {
            LOGGER.error("Could not load employment start data", e);
            return ResponseEntity.notFound().build();
        }
    }
}

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
import ch.prevo.open.node.services.EmploymentTerminationService;

@RestController
public class EmploymentTerminationController {

    private static Logger LOGGER = LoggerFactory.getLogger(EmploymentTerminationController.class);

    private EmploymentTerminationService employmentTerminationService;

    @Inject
    public EmploymentTerminationController(EmploymentTerminationService employmentTerminationService) {
        this.employmentTerminationService = employmentTerminationService;
    }

    @RequestMapping("/termination-of-employment")
    public ResponseEntity<SortedSet<InsurantInformation>> getAllEmploymentTerminationData() {
        try {
            Set<InsurantInformation> allEmploymentTerminationData = this.employmentTerminationService.getAllEmploymentTerminationData();
            return ResponseEntity.ok(new TreeSet<>(allEmploymentTerminationData));
        } catch (Exception e) {
            LOGGER.error("Could not load employment end data", e);
            return ResponseEntity.notFound().build();
        }
    }
}

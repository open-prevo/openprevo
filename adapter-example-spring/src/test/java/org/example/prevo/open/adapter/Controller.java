package org.example.prevo.open.adapter;

import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.data.api.EmploymentCommencement;
import org.example.prevo.open.adapter.dao.EmploymentTerminationDAO;
import org.example.prevo.open.adapter.dao.EmploymentCommencementDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
public class Controller {

    private final EmploymentCommencementDAO employmentStartDAO;
    private final EmploymentTerminationDAO employmentEndDAO;

    @Inject
    public Controller(EmploymentCommencementDAO employmentStartDAO, EmploymentTerminationDAO employmentEndDAO) {
        this.employmentStartDAO = employmentStartDAO;
        this.employmentEndDAO = employmentEndDAO;
    }

    @RequestMapping("/commencement-of-employment")
    public ResponseEntity<List<EmploymentCommencement>> getAllEmploymentCommencementData() {
        return ResponseEntity.ok(employmentStartDAO.getEmploymentCommencements());
    }

    @RequestMapping("/termination-of-employment")
    public ResponseEntity<List<EmploymentTermination>> getAllEmploymentTerminationData() {
        return ResponseEntity.ok(employmentEndDAO.getEmploymentTerminations());
    }
}

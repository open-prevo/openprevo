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

    private final EmploymentCommencementDAO jobStartDAO;
    private final EmploymentTerminationDAO jobEndDAO;

    @Inject
    public Controller(EmploymentCommencementDAO jobStartDAO, EmploymentTerminationDAO jobEndDAO) {
        this.jobStartDAO = jobStartDAO;
        this.jobEndDAO = jobEndDAO;
    }

    @RequestMapping("/commencement-of-employment")
    public ResponseEntity<List<EmploymentCommencement>> getAllEmploymentCommencementData() {
        return ResponseEntity.ok(jobStartDAO.getEmploymentCommencements());
    }

    @RequestMapping("/termination-of-employment")
    public ResponseEntity<List<EmploymentTermination>> getAllEmploymentTerminationData() {
        return ResponseEntity.ok(jobEndDAO.getEmploymentTerminations());
    }
}

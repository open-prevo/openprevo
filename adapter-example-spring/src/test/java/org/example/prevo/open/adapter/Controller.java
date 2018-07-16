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

    private final EmploymentCommencementDAO employmentCommencementDAO;
    private final EmploymentTerminationDAO employmentTerminationDAO;

    @Inject
    public Controller(EmploymentCommencementDAO employmentCommencementDAO, EmploymentTerminationDAO employmentTerminationDAO) {
        this.employmentCommencementDAO = employmentCommencementDAO;
        this.employmentTerminationDAO = employmentTerminationDAO;
    }

    @RequestMapping("/commencement-of-employment")
    public ResponseEntity<List<EmploymentCommencement>> getAllEmploymentCommencementData() {
        return ResponseEntity.ok(employmentCommencementDAO.getEmploymentCommencements());
    }

    @RequestMapping("/termination-of-employment")
    public ResponseEntity<List<EmploymentTermination>> getAllEmploymentTerminationData() {
        return ResponseEntity.ok(employmentTerminationDAO.getEmploymentTerminations());
    }
}

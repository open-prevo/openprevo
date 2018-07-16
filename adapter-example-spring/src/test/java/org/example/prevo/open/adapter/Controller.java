package org.example.prevo.open.adapter;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobStart;
import org.example.prevo.open.adapter.dao.JobEndDAO;
import org.example.prevo.open.adapter.dao.JobStartDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
public class Controller {

    private final JobStartDAO jobStartDAO;
    private final JobEndDAO jobEndDAO;

    @Inject
    public Controller(JobStartDAO jobStartDAO, JobEndDAO jobEndDAO) {
        this.jobStartDAO = jobStartDAO;
        this.jobEndDAO = jobEndDAO;
    }

    @RequestMapping("/commencement-of-employment")
    public ResponseEntity<List<JobStart>> getAllJobStartData() {
        return ResponseEntity.ok(jobStartDAO.getJobStarts());
    }

    @RequestMapping("/termination-of-employment")
    public ResponseEntity<List<JobEnd>> getAllJobEndData() {
        return ResponseEntity.ok(jobEndDAO.getJobEnds());
    }
}

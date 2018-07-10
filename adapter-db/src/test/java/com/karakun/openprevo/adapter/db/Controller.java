package com.karakun.openprevo.adapter.db;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobStart;
import com.karakun.openprevo.adapter.db.dao.JobEndDAO;
import com.karakun.openprevo.adapter.db.dao.JobStartDAO;
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

    @RequestMapping("/job-start")
    public ResponseEntity<List<JobStart>> getAllJobStartData() {
        return ResponseEntity.ok(jobStartDAO.getJobStarts());
    }

    @RequestMapping("/job-end")
    public ResponseEntity<List<JobEnd>> getAllJobEndData() {
        return ResponseEntity.ok(jobEndDAO.getJobEnds());
    }
}

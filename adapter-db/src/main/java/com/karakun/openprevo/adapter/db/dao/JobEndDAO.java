package com.karakun.openprevo.adapter.db.dao;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.node.data.provider.JobEndProvider;
import com.karakun.openprevo.adapter.db.dto.JobEndDTO;
import com.karakun.openprevo.adapter.db.dto.JobInfoDTO;
import com.karakun.openprevo.adapter.db.repository.JobEndRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobEndDAO implements JobEndProvider {

    private final JobEndRepository repository;
    private final ModelMapper mapper;

    @Inject
    public JobEndDAO(JobEndRepository repository) {
        this.repository = repository;
        this.mapper = new ModelMapper();

        final JobInfoDTO jobInfo = new JobInfoDTO();
        jobInfo.setOasiNumber("123");
        jobInfo.setDate(LocalDate.now());
        jobInfo.setRetirementFundUid("4711");

        final JobEndDTO jobEnd = new JobEndDTO();
        jobEnd.setJobInfo(jobInfo);

        repository.save(jobEnd);
        repository.flush();
    }

    @Override
    public List<JobEnd> getJobEnds() {
        return repository.findAll().stream()
                .map(dto -> mapper.map(dto, JobEnd.class))
                .collect(Collectors.toList());
    }
}

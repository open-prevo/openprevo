package org.example.prevo.open.adapter.dao;

import ch.prevo.open.data.api.JobStart;
import ch.prevo.open.node.data.provider.JobStartProvider;
import org.example.prevo.open.adapter.repository.JobStartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobStartDAO implements JobStartProvider {

    private final JobStartRepository repository;
    private final ModelMapper mapper;

    @Inject
    public JobStartDAO(JobStartRepository repository) {
        this.repository = repository;
        this.mapper = new ModelMapper();
    }

    @Override
    public List<JobStart> getJobStarts() {
        return repository.findAll().stream()
                .map(dto -> mapper.map(dto, JobStart.class))
                .collect(Collectors.toList());
    }

}

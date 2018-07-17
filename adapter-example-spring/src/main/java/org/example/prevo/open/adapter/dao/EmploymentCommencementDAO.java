package org.example.prevo.open.adapter.dao;

import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import org.example.prevo.open.adapter.repository.EmploymentCommencementRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmploymentCommencementDAO implements EmploymentCommencementProvider {

    private final EmploymentCommencementRepository repository;
    private final ModelMapper mapper;

    @Inject
    public EmploymentCommencementDAO(EmploymentCommencementRepository repository) {
        this.repository = repository;
        this.mapper = new ModelMapper();
    }

    @Override
    public List<EmploymentCommencement> getEmploymentCommencements() {
        return repository.findAll().stream()
                .map(dto -> mapper.map(dto, EmploymentCommencement.class))
                .collect(Collectors.toList());
    }

}

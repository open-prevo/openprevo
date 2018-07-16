package org.example.prevo.open.adapter.dao;

import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import org.example.prevo.open.adapter.dto.EmploymentTerminationDTO;
import org.example.prevo.open.adapter.dto.EmploymentInfoDTO;
import org.example.prevo.open.adapter.repository.EmploymentTerminationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmploymentTerminationDAO implements EmploymentTerminationProvider {

    private final EmploymentTerminationRepository repository;
    private final ModelMapper mapper;

    @Inject
    public EmploymentTerminationDAO(EmploymentTerminationRepository repository) {
        this.repository = repository;
        this.mapper = new ModelMapper();

        final EmploymentInfoDTO jobInfo = new EmploymentInfoDTO();
        jobInfo.setOasiNumber("123");
        jobInfo.setDate(LocalDate.now());
        jobInfo.setRetirementFundUid("4711");

        final EmploymentTerminationDTO jobEnd = new EmploymentTerminationDTO();
        jobEnd.setEmploymentInfo(jobInfo);

        repository.save(jobEnd);
        repository.flush();
    }

    @Override
    public List<EmploymentTermination> getEmploymentTerminations() {
        return repository.findAll().stream()
                .map(dto -> mapper.map(dto, EmploymentTermination.class))
                .collect(Collectors.toList());
    }
}

package org.example.prevo.open.adapter.repository;

import org.example.prevo.open.adapter.dto.EmploymentTerminationDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmploymentTerminationRepository extends JpaRepository<EmploymentTerminationDTO, Long> {
}

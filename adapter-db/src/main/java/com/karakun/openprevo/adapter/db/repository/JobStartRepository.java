package com.karakun.openprevo.adapter.db.repository;

import com.karakun.openprevo.adapter.db.dto.JobStartDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobStartRepository extends JpaRepository<JobStartDTO, Long> {
}

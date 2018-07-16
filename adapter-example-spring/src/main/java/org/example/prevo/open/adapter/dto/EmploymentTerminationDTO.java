package org.example.prevo.open.adapter.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;

@Entity
public class EmploymentTerminationDTO extends AbstractJobEventDTO {

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .toString();
    }

}

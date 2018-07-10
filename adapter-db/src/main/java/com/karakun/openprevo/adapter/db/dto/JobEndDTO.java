package com.karakun.openprevo.adapter.db.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;

@Entity
public class JobEndDTO extends AbstractJobEventDTO {

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .toString();
    }

}

package org.example.prevo.open.adapter.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
public class AbstractEmploymentEventDTO {

    @Id
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    private EmploymentInfoDTO employmentInfo;

    private String techId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTechId() {
        return techId;
    }

    public void setTechId(String techId) {
        this.techId = techId;
    }

    public EmploymentInfoDTO getEmploymentInfo() {
        return employmentInfo;
    }

    public void setEmploymentInfo(EmploymentInfoDTO employmentInfo) {
        this.employmentInfo = employmentInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("techId", techId)
                .append("employmentInfo", employmentInfo)
                .toString();
    }
}

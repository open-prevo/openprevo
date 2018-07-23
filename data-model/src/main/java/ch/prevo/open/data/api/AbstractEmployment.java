package ch.prevo.open.data.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("unused")
abstract class AbstractEmployment {

    @NotNull
    @Valid
    private EmploymentInfo employmentInfo;

    AbstractEmployment() {
    }

    AbstractEmployment(EmploymentInfo employmentInfo) {
        this.employmentInfo = employmentInfo;
    }

    public EmploymentInfo getEmploymentInfo() {
        return employmentInfo;
    }

    public void setEmploymentInfo(EmploymentInfo employmentInfo) {
        this.employmentInfo = employmentInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("employmentInfo", employmentInfo)
                .toString();
    }
}

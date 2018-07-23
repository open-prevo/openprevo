package ch.prevo.open.data.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("unused")
public class EmploymentTermination extends AbstractEmployment {

    public EmploymentTermination() {
    }

    public EmploymentTermination(EmploymentInfo employmentInfo) {
        super(employmentInfo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AbstractEmployment that = (AbstractEmployment) o;

        return new EqualsBuilder()
                .append(getEmploymentInfo(), that.getEmploymentInfo())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getEmploymentInfo())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .toString();
    }
}

package ch.prevo.open.data.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("unused")
public class EmploymentTermination extends AbstractEmploymentEvent {

    public EmploymentTermination() {
    }

    public EmploymentTermination(String techId, EmploymentInfo employmentInfo) {
        super(techId, employmentInfo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AbstractEmploymentEvent that = (AbstractEmploymentEvent) o;

        return new EqualsBuilder()
                .append(getTechId(), that.getTechId())
                .append(getEmploymentInfo(), that.getEmploymentInfo())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getTechId())
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

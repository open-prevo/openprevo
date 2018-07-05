package ch.prevo.open.data.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("unused")
public class JobEnd extends AbstractJobEvent {

    public JobEnd() {
    }

    public JobEnd(String techId, JobInfo jobInfo) {
        super(techId, jobInfo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AbstractJobEvent that = (AbstractJobEvent) o;

        return new EqualsBuilder()
                .append(getTechId(), that.getTechId())
                .append(getJobInfo(), that.getJobInfo())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getTechId())
                .append(getJobInfo())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .toString();
    }
}

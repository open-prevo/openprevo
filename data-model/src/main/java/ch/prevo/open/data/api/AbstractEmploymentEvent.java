package ch.prevo.open.data.api;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("unused")
abstract class AbstractEmploymentEvent {

    private String techId;
    private EmploymentInfo employmentInfo;

    AbstractEmploymentEvent() {
    }

    AbstractEmploymentEvent(String techId, EmploymentInfo employmentInfo) {
        this.techId = techId;
        this.employmentInfo = employmentInfo;
    }

    public String getTechId() {
        return techId;
    }

    public void setTechId(String techId) {
        this.techId = techId;
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
                .append("techId", techId)
                .append("employmentInfo", employmentInfo)
                .toString();
    }
}

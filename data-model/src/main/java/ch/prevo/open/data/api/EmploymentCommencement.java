package ch.prevo.open.data.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("unused")
public class EmploymentCommencement extends AbstractEmployment {

    @Valid
    @NotNull
    private CapitalTransferInformation capitalTransferInfo;

    public EmploymentCommencement() {}

    public EmploymentCommencement(EmploymentInfo employmentInfo, CapitalTransferInformation capitalTransferInfo) {
		super(employmentInfo);
		this.capitalTransferInfo = capitalTransferInfo;
	}

    public CapitalTransferInformation getCapitalTransferInfo() {
        return capitalTransferInfo;
    }

    public void setCapitalTransferInfo(CapitalTransferInformation capitalTransferInfo) {
        this.capitalTransferInfo = capitalTransferInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EmploymentCommencement employmentCommencement = (EmploymentCommencement) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(capitalTransferInfo, employmentCommencement.capitalTransferInfo)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(capitalTransferInfo)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("capitalTransferInfo", capitalTransferInfo)
                .toString();
    }
}

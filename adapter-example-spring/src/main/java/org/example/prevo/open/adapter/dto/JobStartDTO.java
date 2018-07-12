package org.example.prevo.open.adapter.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class JobStartDTO extends AbstractJobEventDTO {

    @OneToOne(cascade = CascadeType.ALL)
    private CapitalTransferInformationDTO capitalTransferInfo;

    public CapitalTransferInformationDTO getCapitalTransferInfo() {
        return capitalTransferInfo;
    }

    public void setCapitalTransferInfo(CapitalTransferInformationDTO capitalTransferInfo) {
        this.capitalTransferInfo = (CapitalTransferInformationDTO) capitalTransferInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("capitalTransferInfo", capitalTransferInfo)
                .toString();
    }
}

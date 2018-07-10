package com.karakun.openprevo.adapter.db.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class JobInfoDTO {

    @Id
    private long id;
    private String retirementFundUid;
    private String internalReferenz;
    private String oasiNumber;
    private String internalPersonId;
    private LocalDate date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRetirementFundUid() {
        return retirementFundUid;
    }

    public void setRetirementFundUid(String retirementFundUid) {
        this.retirementFundUid = retirementFundUid;
    }

    public String getInternalReferenz() {
        return internalReferenz;
    }

    public void setInternalReferenz(String internalReferenz) {
        this.internalReferenz = internalReferenz;
    }

    public String getOasiNumber() {
        return oasiNumber;
    }

    public void setOasiNumber(String oasiNumber) {
        this.oasiNumber = oasiNumber;
    }

    public String getInternalPersonId() {
        return internalPersonId;
    }

    public void setInternalPersonId(String internalPersonId) {
        this.internalPersonId = internalPersonId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("retirementFundUid", retirementFundUid)
                .append("internalReferenz", internalReferenz)
                .append("oasiNumber", oasiNumber)
                .append("internalPersonId", internalPersonId)
                .append("date", date)
                .toString();
    }
}

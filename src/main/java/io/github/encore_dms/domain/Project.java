package io.github.encore_dms.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class Project extends EntityBase {
    private String name;

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String purpose;

    @Basic
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}

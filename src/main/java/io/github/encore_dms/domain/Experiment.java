package io.github.encore_dms.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Experiment extends EntityBase {
    @ManyToMany(mappedBy = "experiments")
    private Set<Project> projects;

    public Set<Project> getProjects() {
        return projects;
    }

    @Basic
    private String purpose;

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}

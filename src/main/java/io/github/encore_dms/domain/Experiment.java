package io.github.encore_dms.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
public class Experiment extends AbstractTimelineEntity {

    protected Experiment(String purpose, ZonedDateTime startTime, ZonedDateTime endTime) {
        super(startTime, endTime);
        setPurpose(purpose);
    }

    protected Experiment() {

    }

    @Basic
    private String purpose;

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @ManyToMany(mappedBy = "experiments")
    private Set<Project> projects;

    public Set<Project> getProjects() {
        return projects;
    }

}

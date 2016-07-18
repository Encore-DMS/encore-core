package io.github.encore_dms.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
public class Project extends AbstractTimelineEntity {

    protected Project(String name, String purpose, ZonedDateTime startTime, ZonedDateTime endTime) {
        super(startTime, endTime);
        setName(name);
        setPurpose(purpose);
    }

    protected Project() {}

    @Basic
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    private String purpose;

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @ManyToMany
    @OrderBy("startTime ASC")
    private List<Experiment> experiments;

    public Iterable<Experiment> getExperiments() {
        return experiments;
    }

    public String toString() {
        return super.toString() + "Name: " + getName() + "\n";
    }

}

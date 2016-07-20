package io.github.encore_dms;

import io.github.encore_dms.domain.Entity;
import io.github.encore_dms.domain.Project;
import io.github.encore_dms.domain.User;

import javax.persistence.EntityManager;
import java.time.ZonedDateTime;

public class DataContext {

    private EntityManager entityManager;

    public DataContext(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void close() {
        entityManager.close();
    }

    public Iterable<Project> getProjects() {
        return entityManager.createQuery("SELECT p FROM Project p", Project.class).getResultList();
    }

    public Project insertProject(String name, String purpose, ZonedDateTime start, ZonedDateTime end) {
        beginTransaction();
        try {
            Project p = new Project(this, getAuthenticatedUser(), name, purpose, start, end);
            insertEntity(p);
            commitTransaction();
            return p;
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        }
    }

    public void insertEntity(Entity entity) {
        entityManager.persist(entity);
    }

    public User getAuthenticatedUser() {
        return null;
    }

    public void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    public void commitTransaction() {
        entityManager.getTransaction().commit();
    }

    public void rollbackTransaction() {
        entityManager.getTransaction().rollback();
    }

    public void isActiveTransaction() {
        entityManager.getTransaction().isActive();
    }

}

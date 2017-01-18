package io.github.encore_dms;

import io.github.encore_dms.domain.Entity;
import io.github.encore_dms.domain.Project;
import io.github.encore_dms.domain.User;

import javax.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class DataContext {

    private EntityManager entityManager;
    private AtomicInteger transactionCount;

    DataContext(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.transactionCount = new AtomicInteger(0);
    }

    public boolean isOpen() {
        return entityManager.isOpen();
    }

    public void close() {
        if (!isOpen()) {
            return;
        }
        entityManager.close();
    }

    public Stream<Project> getProjects() {
        return entityManager.createQuery("SELECT p FROM Project p ORDER BY p.startTime ASC", Project.class).getResultList().stream();
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
        if (!isActiveTransaction()) {
            entityManager.getTransaction().begin();
        }
        transactionCount.incrementAndGet();
    }

    public void commitTransaction() {
        if (transactionCount.get() == 1) {
            entityManager.getTransaction().commit();
        }
        transactionCount.decrementAndGet();
    }

    public void rollbackTransaction() {
        while (transactionCount.get() > 0) {
            entityManager.getTransaction().rollback();
            transactionCount.decrementAndGet();
        }
    }

    public boolean isActiveTransaction() {
        return entityManager.getTransaction().isActive();
    }

}

package io.github.encore_dms.domain.jpa;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.Entity;
import io.github.encore_dms.domain.Project;
import io.github.encore_dms.domain.User;

import javax.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

class JPADataContext implements DataContext {

    private EntityManager entityManager;
    private AtomicInteger transactionCount;

    JPADataContext(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.transactionCount = new AtomicInteger(0);
    }

    @Override
    public boolean isOpen() {
        return entityManager.isOpen();
    }

    @Override
    public void close() {
        if (!isOpen()) {
            return;
        }
        entityManager.close();
    }

    @Override
    public Stream<Project> getProjects() {
        return entityManager.createQuery("SELECT p FROM Project p ORDER BY p.startTime ASC", Project.class).getResultList().stream();
    }

    @Override
    public JPAProject insertProject(String name, String purpose, ZonedDateTime start, ZonedDateTime end) {
        beginTransaction();
        try {
            JPAProject p = new JPAProject(this, getAuthenticatedUser(), name, purpose, start, end);
            insertEntity(p);
            commitTransaction();
            return p;
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        }
    }

    @Override
    public void insertEntity(Entity entity) {
        entityManager.persist(entity);
    }

    @Override
    public User getAuthenticatedUser() {
        return null;
    }

    @Override
    public void beginTransaction() {
        if (!isActiveTransaction()) {
            entityManager.getTransaction().begin();
        }
        transactionCount.incrementAndGet();
    }

    @Override
    public void commitTransaction() {
        if (transactionCount.get() == 1) {
            entityManager.getTransaction().commit();
        }
        transactionCount.decrementAndGet();
    }

    @Override
    public void rollbackTransaction() {
        while (transactionCount.get() > 0) {
            entityManager.getTransaction().rollback();
            transactionCount.decrementAndGet();
        }
    }

    @Override
    public boolean isActiveTransaction() {
        return entityManager.getTransaction().isActive();
    }

}

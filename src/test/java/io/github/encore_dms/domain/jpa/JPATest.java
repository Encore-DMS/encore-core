package io.github.encore_dms.domain.jpa;

import io.github.encore_dms.DataContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

abstract class JPATest {

    private EntityManagerFactory entityManagerFactory;
    @SuppressWarnings("WeakerAccess")
    EntityManager entityManager; // To inspect the db: entityManager.unwrap(Session.class).doWork(Server::startWebServer)
    DataContext context;

    @BeforeEach
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("Test");
        entityManager = entityManagerFactory.createEntityManager();
        context = new JPADataContext(entityManager);
    }

    @AfterEach
    public void tearDown() throws Exception {
        context.close();
        entityManagerFactory.close();
    }

}

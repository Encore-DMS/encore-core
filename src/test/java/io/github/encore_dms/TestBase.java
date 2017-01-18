package io.github.encore_dms;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class TestBase {

    private EntityManagerFactory entityManagerFactory;
    @SuppressWarnings("WeakerAccess")
    protected EntityManager entityManager; // To inspect the db: entityManager.unwrap(Session.class).doWork(Server::startWebServer)
    protected DataContext context;

    @BeforeEach
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("Test");
        entityManager = entityManagerFactory.createEntityManager();
        context = new DataContext(entityManager);
    }

    @AfterEach
    public void tearDown() throws Exception {
        context.close();
        entityManagerFactory.close();
    }

}

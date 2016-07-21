package io.github.encore_dms.domain.jpa;

import io.github.encore_dms.DataContext;
import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

abstract class AbstractJPADomainTest {

    private EntityManagerFactory entityManagerFactory;
    @SuppressWarnings("WeakerAccess")
    EntityManager entityManager; // To inspect the db: entityManager.unwrap(Session.class).doWork(Server::startWebServer)
    DataContext context;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
        entityManager = entityManagerFactory.createEntityManager();
        context = new JPADataContext(entityManager);
    }

    @After
    public void tearDown() throws Exception {
        context.close();
        entityManagerFactory.close();
    }

}

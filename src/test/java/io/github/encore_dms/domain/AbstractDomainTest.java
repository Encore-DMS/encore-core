package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

abstract class AbstractDomainTest {

    private EntityManagerFactory entityManagerFactory;
    DataContext context;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
        context = new DataContext(entityManagerFactory.createEntityManager());
    }

    @After
    public void tearDown() throws Exception {
        context.close();
        entityManagerFactory.close();
    }

}

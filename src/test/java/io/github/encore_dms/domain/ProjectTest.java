package io.github.encore_dms.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

public class ProjectTest {

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
    }

    @After
    public void tearDown() throws Exception {
        entityManagerFactory.close();
    }

    @Test
    public void testBasicUsage() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(new Project());
        entityManager.persist(new Project());
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Project> result = entityManager.createQuery("from Project", Project.class).getResultList();
        for (Project project : result) {
            System.out.println(project.getUuid());
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
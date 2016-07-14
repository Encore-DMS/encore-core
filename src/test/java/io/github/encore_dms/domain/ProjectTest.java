package io.github.encore_dms.domain;

import org.h2.tools.Server;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.ZonedDateTime;
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
        entityManager.persist(new Project("test1", "first testing", ZonedDateTime.now(), ZonedDateTime.now()));
        entityManager.persist(new Project("test2", "second testing", ZonedDateTime.now(), ZonedDateTime.now()));
        entityManager.getTransaction().commit();
        entityManager.close();

        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Project> result = entityManager.createQuery("from Project", Project.class).getResultList();
        for (Project project : result) {
            System.out.println(project);
        }
        entityManager.getTransaction().commit();

        // allows viewing contents of db in web browser
        //entityManager.unwrap(Session.class).doWork(Server::startWebServer);

        entityManager.close();
    }
}
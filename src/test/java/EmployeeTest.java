import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EmployeeTest {

    private EntityManager em;

    @Before
    public void setUp() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NewPersistenceUnit");
        em = emf.createEntityManager();
    }

    @After
    public void tearDown() {
        if (em != null) {
            em.close();
        }
    }

    @Test public void testPersistEmployee() {
        Employee emp = new Employee(158);
        em.persist(emp);
    }
}

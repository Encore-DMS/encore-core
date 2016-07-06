import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class EmployeeTest {

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
    }

    @After
    public void tearDown() {
        entityManagerFactory.close();
    }

    @Test
    public void testBasicUsage() {
        // create a couple of employees...
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(new Employee(128));
        entityManager.persist(new Employee(129));
        entityManager.getTransaction().commit();
        entityManager.close();

        // now lets pull employees from the database and list them
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Employee> result = entityManager.createQuery("from Employee", Employee.class).getResultList();
        for (Employee employee : result) {
            System.out.println("Employee(" + employee.getId() + ")");
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

}

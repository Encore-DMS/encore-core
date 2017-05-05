package io.github.encore_dms.data;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.github.encore_dms.DefaultTransactionManager;
import io.github.encore_dms.TransactionManager;
import io.github.encore_dms.domain.User;
import io.github.encore_dms.util.TransactionUtilities;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class InMemoryDataStore extends AbstractDataStore {

    private final EntityDao dao;
    private final TransactionManager transactionManager;

    @Inject
    public InMemoryDataStore(@Assisted("host") String host, @Assisted("username") String username, @Assisted("password") String password) {
        super("jdbc:h2:mem:" + host, username, password);

        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.provider", "org.hibernate.jpa.HibernatePersistenceProvider");
        properties.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
        properties.put("javax.persistence.jdbc.driver", "org.h2.Driver");
        properties.put("javax.persistence.jdbc.url", getUrl());
        properties.put("javax.persistence.jdbc.user", getUsername());
        properties.put("javax.persistence.jdbc.password", getPassword());
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.show_sql", "false");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("Main", properties);
        EntityManager entityManager = factory.createEntityManager();

        dao = new DefaultEntityDao(entityManager.unwrap(Session.class));
        transactionManager = new DefaultTransactionManager(entityManager.getTransaction());

        TransactionUtilities.transactionWrapped(transactionManager, () -> {
            User u = new User(null, getUsername(), getPassword());
            dao.persist(u);
        });
    }

    @Override
    public EntityDao getDao() {
        return dao;
    }

    @Override
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }
}

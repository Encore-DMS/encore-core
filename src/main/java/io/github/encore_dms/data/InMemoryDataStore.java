package io.github.encore_dms.data;

import io.github.encore_dms.DefaultTransactionManager;
import io.github.encore_dms.TransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class InMemoryDataStore extends AbstractDataStore {

    private final EntityDao dao;
    private final TransactionManager transactionManager;

    public InMemoryDataStore() {
        super("jdbc:h2:mem:default", "sa", "");

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

        dao = new DefaultEntityDao(entityManager);
        transactionManager = new DefaultTransactionManager(entityManager.getTransaction());
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

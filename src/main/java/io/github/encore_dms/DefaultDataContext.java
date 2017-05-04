package io.github.encore_dms;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.physion.ebuilder.expression.ExpressionTree;
import com.physion.ebuilder.expression.IExpression;
import io.github.encore_dms.data.DataStore;
import io.github.encore_dms.data.EntityDao;
import io.github.encore_dms.domain.Entity;
import io.github.encore_dms.domain.EntityRepository;
import io.github.encore_dms.domain.Project;
import io.github.encore_dms.domain.User;
import io.github.encore_dms.exceptions.EncoreException;
import io.github.encore_dms.util.ExpressionUtilities;
import io.github.encore_dms.util.TransactionUtilities;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

public class DefaultDataContext implements DataContext {

    private final DataStoreCoordinator dataStoreCoordinator;
    private final EntityDao dao;
    private final EntityRepository entityRepository;
    private final TransactionManager transactionManager;

    private User authenticatedUser;

    @Inject
    DefaultDataContext(@Assisted DataStore dataStore, @Assisted DataStoreCoordinator dataStoreCoordinator, EntityRepository.Factory entityRepositoryFactory) {
        this.dataStoreCoordinator = dataStoreCoordinator;
        this.dao = dataStore.getDao();
        this.entityRepository = entityRepositoryFactory.create(this.dao, this);
        this.transactionManager = dataStore.getTransactionManager();
    }

    @Override
    public <T extends Entity> Stream<T> query(String qlString, Class<T> resultClass) {
        return dao.query(qlString, resultClass);
    }

    @Override
    public <T extends Entity> Stream<T> query(ExpressionTree expressionTree) {
        String entityName = expressionTree.getClassUnderQualification();
        IExpression expression = expressionTree.getRootExpression();
        try {
            return query(ExpressionUtilities.generateJpql(expression), (Class<T>) Class.forName(entityName));
        } catch (ClassNotFoundException e) {
            throw new EncoreException(entityName + " does not exist in Encore database.");
        }
    }

    @Override
    public EntityRepository getRepository() {
        return this.entityRepository;
    }

    @Override
    public DataStoreCoordinator getCoordinator() {
        return this.dataStoreCoordinator;
    }

    @Override
    public Project insertProject(String name, String purpose, ZonedDateTime start, ZonedDateTime end) {
        return TransactionUtilities.transactionWrapped(this, () -> {
            Project p = new Project(DefaultDataContext.this, getAuthenticatedUser(), name, purpose, start, end);
            insertEntity(p);
            return p;
        });
    }

    @Override
    public Stream<Project> getProjects() {
        return getRepository().getProjects();
    }

    @Override
    public void insertEntity(Entity entity) {
        TransactionUtilities.transactionWrapped(this, () -> getRepository().persist(entity));
    }

    @Override
    public User getAuthenticatedUser() {
        String name = dataStoreCoordinator.getAuthenticatedUser();
        if (name == null) {
            return null;
        }
        if (authenticatedUser == null) {
            authenticatedUser = entityRepository.getUserWithUsername(name);
        }
        return authenticatedUser;
    }

    @Override
    public void beginTransaction() {
        transactionManager.beginTransaction();
    }

    @Override
    public void commitTransaction() {
        transactionManager.commitTransaction();
    }

    @Override
    public void rollbackTransaction() {
        transactionManager.rollbackTransaction();
    }

    @Override
    public boolean isActiveTransaction() {
        return transactionManager.isActiveTransaction();
    }

}

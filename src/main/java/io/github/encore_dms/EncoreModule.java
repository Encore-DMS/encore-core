package io.github.encore_dms;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import io.github.encore_dms.data.DataStore;
import io.github.encore_dms.data.InMemoryDataStore;
import io.github.encore_dms.domain.DefaultEntityRepository;
import io.github.encore_dms.domain.EntityRepository;

public class EncoreModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DataStoreCoordinator.Connection.class).to(DefaultDataStoreCoordinatorConnection.class);

        install(new FactoryModuleBuilder()
                .implement(DataStoreCoordinator.class, DefaultDataStoreCoordinator.class)
                .build(DataStoreCoordinator.Factory.class));

        install(new FactoryModuleBuilder()
                .implement(DataStore.class, InMemoryDataStore.class)
                .build(DataStore.Factory.class));

        install(new FactoryModuleBuilder()
                .implement(DataContext.class, DefaultDataContext.class)
                .build(DataContext.Factory.class));

        install(new FactoryModuleBuilder()
                .implement(EntityRepository.class, DefaultEntityRepository.class)
                .build(EntityRepository.Factory.class));
    }

}

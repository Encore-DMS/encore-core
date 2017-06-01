package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.ResourceContainer;
import io.github.encore_dms.exceptions.EncoreException;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class AbstractResourceAnnotatableEntity extends AbstractAnnotatableEntity implements ResourceContainer {

    AbstractResourceAnnotatableEntity(DataContext context, User owner) {
        super(context, owner);
        resources = new HashMap<>();
    }

    protected AbstractResourceAnnotatableEntity() {}

    @OneToMany
    @MapKey(name = "name")
    private Map<String, Resource> resources;

    @Override
    public Resource addResource(String name, byte[] data, String uti) {
        if (resources.containsKey(name))
            throw new EncoreException("Resource with the given name already exists");

        return transactionWrapped(() -> {
            DataContext c = getDataContext();
            User user = c.getAuthenticatedUser();
            Resource resource = new Resource(c, user, name, data, uti);
            c.insertEntity(resource);
            resources.put(resource.getName(), resource);
            return resource;
        });
    }

    @Override
    public void removeResource(String name) {
        if (!resources.containsKey(name))
            throw new EncoreException("A resource with the name '" + name + "' does not exist");

        transactionWrapped(() -> {
            resources.remove(name);
        });
    }

    @Override
    public Stream<Resource> getResources() {
        return resources.values().stream();
    }

    @Override
    public Resource getResource(String name) {
        return resources.get(name);
    }

    @Override
    public Stream<String> getResourceNames() {
        return resources.keySet().stream();
    }
}

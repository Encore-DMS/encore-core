package io.github.encore_dms.domain.mixin;

import io.github.encore_dms.domain.Resource;

import java.util.stream.Stream;

public interface ResourceContainer {

    Resource addResource(String name, byte[] data, String uti);

    void removeResource(String name);

    Stream<Resource> getResources();

    Resource getResource(String name);

    Stream<String> getResourceNames();

}

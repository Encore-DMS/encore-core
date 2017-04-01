package io.github.encore_dms.domain.mixin;

import io.github.encore_dms.domain.User;

import java.util.Map;

public interface PropertyAnnotatable {

    void addProperty(String key, Object value);

    void removeProperty(String key);

    Map<User, Map<String, Object>> getProperties();

}

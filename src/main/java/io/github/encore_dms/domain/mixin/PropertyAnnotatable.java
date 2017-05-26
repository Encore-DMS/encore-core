package io.github.encore_dms.domain.mixin;

import io.github.encore_dms.domain.User;

import java.io.Serializable;
import java.util.Map;

public interface PropertyAnnotatable {

    void addProperty(String key, Serializable value);

    void addProperty(String key, Object serializableValue);

    void removeProperty(String key);

    Map<User, Map<String, Object>> getProperties();

    Map<User, Object> getProperty(String key);

    Object getUserProperty(User user, String key);

    Map<String, Object> getUserProperties(User user);

}

package io.github.encore_dms.domain.mixin;

import io.github.encore_dms.domain.User;

import java.io.Serializable;
import java.util.Map;

public interface PropertyAnnotatable {

    void addProperty(String key, Serializable value);

    void removeProperty(String key);

    Map<User, Map<String, Serializable>> getProperties();

    Map<User, Serializable> getProperty(String key);

    Serializable getUserProperty(User user, String key);

    Map<String, Serializable> getUserProperties(User user);

}

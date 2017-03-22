package io.github.encore_dms.domain.mixin;

import io.github.encore_dms.domain.User;

import java.util.Map;

public interface KeywordAnnotatable {

    Map<User, String> getKeywords();

    void addKeyword(String keyword);

    void removeKeyword(String keyword);

}

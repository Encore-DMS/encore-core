package io.github.encore_dms.domain.mixin;

import com.google.common.collect.Multimap;
import io.github.encore_dms.domain.User;

public interface KeywordAnnotatable {

    void addKeyword(String keyword);

    void removeKeyword(String keyword);

    Multimap<User, String> getKeywords();

}

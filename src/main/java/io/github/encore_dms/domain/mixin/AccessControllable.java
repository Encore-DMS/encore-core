package io.github.encore_dms.domain.mixin;

import io.github.encore_dms.domain.User;

public interface AccessControllable {

    boolean canRead(User user);

    boolean canWrite(User user);

}

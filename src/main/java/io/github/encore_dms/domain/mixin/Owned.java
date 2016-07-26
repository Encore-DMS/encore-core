package io.github.encore_dms.domain.mixin;

import io.github.encore_dms.domain.User;

public interface Owned extends AccessControllable {

    User getOwner();

}

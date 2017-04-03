package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.AccessControllable;
import io.github.encore_dms.domain.mixin.Identity;
import io.github.encore_dms.domain.mixin.Owned;

public interface Entity extends Identity, Owned, AccessControllable {

    DataContext getDataContext();

}

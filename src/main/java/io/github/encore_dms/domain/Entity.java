package io.github.encore_dms.domain;

import io.github.encore_dms.DataContext;
import io.github.encore_dms.domain.mixin.AccessControllable;
import io.github.encore_dms.domain.mixin.Identity;

public interface Entity extends Identity, AccessControllable {

    DataContext getDataContext();

}

package io.github.encore_dms.domain;

import java.util.UUID;

public interface Entity {

    User getOwner();

    UUID getUuid();

}

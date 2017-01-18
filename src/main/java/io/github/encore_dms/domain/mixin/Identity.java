package io.github.encore_dms.domain.mixin;

import java.net.URI;
import java.util.UUID;

public interface Identity {

    UUID getUuid();

    URI getUri();

}

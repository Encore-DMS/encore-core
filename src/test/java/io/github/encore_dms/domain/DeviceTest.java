package io.github.encore_dms.domain;

import io.github.encore_dms.AbstractTest;
import io.github.encore_dms.DataContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DeviceTest extends AbstractTest {

    private Device device;

    @Mock
    private DataContext context;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        device = new Device(context, null, null, "name", "manufacturer");
    }

}

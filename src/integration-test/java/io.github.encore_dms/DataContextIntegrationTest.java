package io.github.encore_dms;

import org.junit.jupiter.api.Test;

public class DataContextIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void test() {
        DataStoreCoordinator coordinator = Encore.connect("localhost", "sa", "");
    }

}

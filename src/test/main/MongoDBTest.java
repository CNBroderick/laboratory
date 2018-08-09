package main;

import org.bklab.vaadin.mongo.MongoManager;
import org.junit.Test;

public class MongoDBTest {

    @Test
    public void testConnectivity() {
        new MongoManager();
    }
}

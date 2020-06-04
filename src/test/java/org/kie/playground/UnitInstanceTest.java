package org.kie.playground;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class UnitInstanceTest {
    @Test
    public void checkContents() {
        var c = new MyContext();
        var u = Unit.create(c);
        c.ds.add("a");
        c.ds.add("b");
        c.ds.add("c");
        var ui = u.createInstance();
        ui.run();
        

    }
}

class MyContext {
    DataStore<String> ds = ListDataStore.create();
}
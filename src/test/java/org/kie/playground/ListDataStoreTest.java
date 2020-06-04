package org.kie.playground;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class ListDataStoreTest {

    @Test
    public void checkContents() {
        var ds = new ListDataStore<String>();
        var sub = new RecordingSubscriber<String>();
        ds.subscribe(sub);
        ds.add("a");
        ds.add("b");
        ds.add("c");
        assertEquals(List.of("a", "b", "c"), sub.getData());
    }
}

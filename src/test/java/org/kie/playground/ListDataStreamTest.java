package org.kie.playground;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for simple App.
 */
public class ListDataStreamTest {

    @Test
    public void checkContents() {
        var ds = new ListDataStream<String>();
        var sub = new RecordingSubscriber<String>();
        ds.subscribe(sub);
        ds.append("a");
        ds.append("b");
        ds.append("c");
        assertEquals(List.of("a", "b", "c"), sub.getData());
    }
}

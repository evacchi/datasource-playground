package org.kie.playground;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeliefTest {
    @Test
    public void checkContents() {
        var hello1 = DataHandle.of("Hello");
        var hello2 = DataHandle.of("Hello");
        var hi1 = DataHandle.of("Hi");
        var hi2 = DataHandle.of("Hi");

        var ds = new Belief<String>();
        var rec = new RecordingSubscriber<String>();
        ds.subscribe(rec);

        ds.stated().added(hello1);
        ds.stated().added(hello2);
        ds.justified().added(hi1);
        ds.justified().added(hi2);

        assertEquals(List.of("Hello", "Hello", "Hi"), rec.getData());
    }
}

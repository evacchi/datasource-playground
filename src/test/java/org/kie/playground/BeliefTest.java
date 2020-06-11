package org.kie.playground;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeliefTest {

    @Test
    public void checkContents() {
        var stated = new ListDataStore<String>();
        var justified = new ListDataStore<String>();

        var hello1 = "Hello";
        var hello2 = "Hello";
        var hi1 = "Hi";
        var hi2 = "Hi";

        var ds = new Belief<String>();
        var rec = new RecordingSubscriber<String>();
        ds.subscribe(rec);

        stated.subscribe(ds.stated());
        justified.subscribe(ds.justified());
        stated.add(hello1);
        stated.add(hello2);
        justified.add(hi1);
        justified.add(hi2);

        assertEquals(List.of("Hello", "Hello", "Hi"), rec.getData());
    }
}

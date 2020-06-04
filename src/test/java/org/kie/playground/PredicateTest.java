package org.kie.playground;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PredicateTest {
    @Test
    public void checkContents() {
        var ds = new ListDataStream<String>();
        // prepend "x" to all strings it sees
        var f = PredicateFilter.of((String s) -> s.matches("[aeiouAEIOU]"));

        ds.subscribe(f);

        var rec = new RecordingSubscriber<String>();
        f.subscribe(rec);

        ds.append("a");
        ds.append("b");
        ds.append("c");


        assertEquals(List.of("a"), rec.getData());
    }
}

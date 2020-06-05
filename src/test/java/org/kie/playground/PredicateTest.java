package org.kie.playground;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PredicateTest {

    @Test
    public void filter1() {
        var ds = new ListDataStream<String>();
        // keep vowels
        var f = PredicateFilter.of((String s) -> s.matches("[aeiouAEIOU]"));

        ds.subscribe(f);

        var rec = new RecordingSubscriber<String>();
        f.subscribe(rec);

        ds.append("a");
        ds.append("b");
        ds.append("c");

        assertEquals(List.of("a"), rec.getData());
    }

    /**
     * strings
     * /  \
     * vowels  non-vowels
     */
    @Test
    public void filter2() {
        var ds = new ListDataStream<String>();

        // keep vowels
        var f1 = PredicateFilter.of((String s) -> s.matches("[aeiouAEIOU]"));
        // keep others
        var f2 = PredicateFilter.of((String s) -> s.matches("[^aeiouAEIOU]"));

        ds.subscribe(f1);
        ds.subscribe(f2);

        var rec1 = new RecordingSubscriber<String>();
        f1.subscribe(rec1);

        var rec2 = new RecordingSubscriber<String>();
        f2.subscribe(rec2);

        ds.append("a");
        ds.append("b");
        ds.append("c");
        ds.append("1");

        assertEquals(List.of("a"), rec1.getData());
        assertEquals(List.of("b", "c", "1"), rec2.getData());
    }
}

package org.kie.playground;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PredicateTest {

    @Test
    public void filter1() {
        var source = new ListDataStream<String>();
        // keep vowels
        var f = PredicateFilter.of((String s) -> s.matches("[aeiouAEIOU]"));

        source.subscribe(f);

        var sink = new RecordingSubscriber<String>();
        f.subscribe(sink);

        source.append("a");
        source.append("b");
        source.append("c");

        assertEquals(List.of("a"), sink.getData());
    }

    /**
     * strings
     * /  \
     * vowels  non-vowels
     */
    @Test
    public void filter2() {
        var source = new ListDataStream<String>();

        // keep vowels
        var f1 = PredicateFilter.of((String s) -> s.matches("[aeiouAEIOU]"));
        // keep others
        var f2 = PredicateFilter.of((String s) -> s.matches("[^aeiouAEIOU]"));

        source.subscribe(f1);
        source.subscribe(f2);

        var sink1 = new RecordingSubscriber<String>();
        f1.subscribe(sink1);

        var sink2 = new RecordingSubscriber<String>();
        f2.subscribe(sink2);

        source.append("a");
        source.append("b");
        source.append("c");
        source.append("1");

        /*
                    source <---- append
                  /     \
                f1      f2
                |       |
              sink1     sink2
         */

        assertEquals(List.of("a"), sink1.getData());
        assertEquals(List.of("b", "c", "1"), sink2.getData());
    }

    @Test
    public void filter3() {
        var source = new ListDataStream<String>();
        var sink1 = new RecordingSubscriber<String>();
        var sink2 = new RecordingSubscriber<String>();
        var sink3 = new RecordingSubscriber<String>();

        // keep vowels
        var f1 = PredicateFilter.of((String s) -> s.matches("[aeiouAEIOU]"));
        // keep others
        var f2 = PredicateFilter.of((String s) -> s.matches("[^aeiouAEIOU]"));
        // keep numbers
        var f3 = PredicateFilter.of((String s) -> s.matches("[0-9]"));


        source.subscribe(f1);
        source.subscribe(f2);

        f1.subscribe(sink1);

        f2.subscribe(sink2);

        f2.subscribe(f3);

        f3.subscribe(sink3);

        source.append("a");
        source.append("b");
        source.append("c");
        source.append("1");

        /*
                   source <---- append
                  /      \
                f1       f2
                |      /   \
              sink1  sink2  f3
                             \
                            sink3
         */

        assertEquals(List.of("a"), sink1.getData());
        assertEquals(List.of("b", "c", "1"), sink2.getData());
        assertEquals(List.of("1"), sink3.getData());
    }
}

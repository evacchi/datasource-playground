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
        var sink = new RecordingSubscriber<String>();

        source.subscribe(f);
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
        var sink1 = new RecordingSubscriber<String>();
        var sink2 = new RecordingSubscriber<String>();

        // keep vowels
        var vowels = PredicateFilter.of((String s) -> s.matches("[aeiouAEIOU]"));
        // keep others
        var nonVowels = PredicateFilter.of((String s) -> s.matches("[^aeiouAEIOU]"));

        source.subscribe(vowels);
        source.subscribe(nonVowels);

        vowels.subscribe(sink1);
        nonVowels.subscribe(sink2);

        /*
                    source <---- append
                  /        \
                vowels   nonVowels
                |           |
              sink1       sink2
         */

        source.append("a");
        source.append("b");
        source.append("c");
        source.append("1");

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
        var vowels = PredicateFilter.of((String s) -> s.matches("[aeiouAEIOU]"));
        // keep others
        var nonVowels = PredicateFilter.of((String s) -> s.matches("[^aeiouAEIOU]"));
        // keep numbers
        var digits = PredicateFilter.of((String s) -> s.matches("[0-9]"));

        source.subscribe(vowels);
        source.subscribe(nonVowels);
        nonVowels.subscribe(digits);

        vowels.subscribe(sink1);
        nonVowels.subscribe(sink2);
        digits.subscribe(sink3);

        /*
                   source <---- append
                  /      \
              vowels   nonVowels
                |      /   \
             sink1  sink2  digits
                             \
                            sink3
         */

        source.append("a");
        source.append("b");
        source.append("c");
        source.append("1");

        assertEquals(List.of("a"), sink1.getData());
        assertEquals(List.of("b", "c", "1"), sink2.getData());
        assertEquals(List.of("1"), sink3.getData());
    }
}

package org.kie.playground;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtendedPredicateTest {

    static class Restaurant {

        private final String location;

        Restaurant(String location) {
            this.location = location;
        }

        public String getLocation() {
            return location;
        }

        @Override
        public String toString() {
            return "Restaurant{" +
                    "location='" + location + '\'' +
                    '}';
        }
    }

    @Test
    public void filter1() {
        var source = new ExtendedListDataStream<Restaurant>(
                List.of(r -> r.getLocation().equals("paris"),
                        r -> r.getLocation().equals("london")),
                List.of(new LambdaIndex<>(Restaurant::getLocation))
        );
        var sink = new RecordingSubscriber<Restaurant>();

        source.subscribe(sink);

        var paris = new Restaurant("paris");
        var rome = new Restaurant("rome");
        var london = new Restaurant("london");

        source.append(paris);
        source.append(rome);
        source.append(london);

        assertEquals(List.of(paris, london), sink.getData());
    }

    @Test
    public void filter2() {
        var source = new ExtendedListDataStream<Restaurant>(
                List.of(r -> r.getLocation().equals("paris"),
                        r -> r.getLocation().equals("london")),
                List.of(new LambdaIndex<>(Restaurant::getLocation))
        );
        var sink = new RecordingSubscriber<Restaurant>();

        source.subscribe(sink, 0);

        var paris = new Restaurant("paris");
        var rome = new Restaurant("rome");
        var london = new Restaurant("london");

        source.append(paris);
        source.append(rome);
        source.append(london);

        assertEquals(List.of(paris, london), sink.getData());
    }

}

package org.kie.playground;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtendedPredicateTest {

    static class Restaurant {

        private final String name;
        private final String location;
        private int tables;

        Restaurant(String name, String location, int tables) {
            this.name = name;
            this.location = location;
            this.tables = tables;
        }

        public String getName() {
            return name;
        }

        public int getTables() {
            return tables;
        }

        public String getLocation() {
            return location;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Restaurant{");
            sb.append("name='").append(name).append('\'');
            sb.append(", location='").append(location).append('\'');
            sb.append(", tables=").append(tables);
            sb.append('}');
            return sb.toString();
        }
    }

    @Test
    public void filter1() {
        var index = LambdaIndex.of(Restaurant::getLocation);
        PredicateFilter<Restaurant> paris100 = PredicateFilter.of(r -> r.getTables() >= 100);
        PredicateFilter<Restaurant> london100 = PredicateFilter.of(r -> r.getTables() >= 100);
        PredicateFilter<Restaurant> all100 = PredicateFilter.of(r -> r.getTables() >= 100);
        index.subscribe("paris", paris100);
        index.subscribe("london", london100);
        index.subscribe(all100);

        var source = new ExtendedListDataStore<Restaurant>(List.of(index));
        var sink = new RecordingSubscriber<Restaurant>();

        var sinkParis100 = new RecordingSubscriber<Restaurant>();
        var sinkLondon100 = new RecordingSubscriber<Restaurant>();
        var sinkAll100 = new RecordingSubscriber<Restaurant>();

        source.subscribe(sink);
        paris100.subscribe(sinkParis100);
        london100.subscribe(sinkLondon100);
        all100.subscribe(sinkAll100);

        var chatNoir = new Restaurant("Le Chat Noir", "paris", 100);
        var riveGauche = new Restaurant("La Rive Gauche", "paris", 10);
        var soraLella = new Restaurant("Sora Lella", "rome", 30);
        var flatIron = new Restaurant("Flat Iron", "london", 120);

        source.add(chatNoir);
        source.add(riveGauche);
        source.add(soraLella);
        source.add(flatIron);

        assertEquals(List.of(chatNoir, riveGauche, soraLella, flatIron), sink.getData());
        assertEquals(List.of(chatNoir), sinkParis100.getData());
        assertEquals(List.of(flatIron), sinkLondon100.getData());
        assertEquals(List.of(chatNoir, flatIron), sinkAll100.getData());
    }

    @Test
    public void join() {
        var rightSource = source(String.class);
        var rightSink = sink(String.class);
        rightSource.subscribe(rightSink);
        rightSource.add("Sora Lella");
        rightSource.add("La Rive Gauche");

        assertEquals(List.of("Sora Lella", "La Rive Gauche"), rightSink.getData());

        var index = LambdaIndex.of(Restaurant::getLocation);
        PredicateFilter<Restaurant> paris100 = PredicateFilter.of(r -> r.getTables() >= 100);
        index.subscribe("paris", paris100);

        var source = source(List.of(index));
        var sink = sink(Restaurant.class);

        var sinkParis100 = new RecordingSubscriber<Restaurant>();

        source.subscribe(sink);
        paris100.subscribe(sinkParis100);

        var myJoin = new FanOut<Restaurant>() {
            @Override
            public void added(DataHandle<Restaurant> dh) {
                // notify only those whose name is in rightSink (paris, london, rome)
                rightSink.getData().stream().filter(d -> d.equals(dh.getObject().getName())).forEach(v -> super.added(dh));
            }
        };
        var joinSink = sink(Restaurant.class);

        index.subscribe(myJoin);
        myJoin.subscribe(joinSink);

        var chatNoir = new Restaurant("Le Chat Noir", "paris", 100);
        var riveGauche = new Restaurant("La Rive Gauche", "paris", 10);
        var soraLella = new Restaurant("Sora Lella", "rome", 30);
        var flatIron = new Restaurant("Flat Iron", "london", 120);

        source.add(chatNoir);
        source.add(riveGauche);
        source.add(soraLella);
        source.add(flatIron);

        assertEquals(List.of(chatNoir, riveGauche, soraLella, flatIron), sink.getData());
        assertEquals(List.of(chatNoir), sinkParis100.getData());
        assertEquals(List.of(riveGauche, soraLella), joinSink.getData());
    }

    private <T, R> ExtendedListDataStore<T> source(Collection<Index<T, ?>> index) {
        return new ExtendedListDataStore<T>(index);
    }

    private <T> ExtendedListDataStore<T> source(Class<T> cls) {
        return new ExtendedListDataStore<>();
    }

    private <T> RecordingSubscriber<T> sink(Class<T> cls) {
        return new RecordingSubscriber<>();
    }
}

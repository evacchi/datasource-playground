package org.kie.playground;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

public class PredicateFilter<T> implements Filter<T> {

    private final ArrayList<Subscriber<T>> subscribers = new ArrayList<>();
    private final Predicate<T> predicate;

    public static <T> PredicateFilter<T> of(Predicate<T> predicate) {
        return new PredicateFilter<>(predicate);
    }

    PredicateFilter(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    public void subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void added(DataHandle<T> dh) {
        subscribers.forEach(s -> {
            if (predicate.test(dh.getObject())) {
                s.added(dh);
            }
        });
    }

    @Override
    public void removed(DataHandle<T> dh) {
        // ?
    }
}

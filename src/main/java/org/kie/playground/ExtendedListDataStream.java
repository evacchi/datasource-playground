package org.kie.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.function.Predicate;

public class ExtendedListDataStream<T> implements DataStream<T> {

    private final IdentityHashMap<Object, DataHandle<T>> store = new IdentityHashMap<>();
    private final ArrayList<DataHandle<T>> order = new ArrayList<>();
    private final ArrayList<Subscriber<T>> subscribers = new ArrayList<>();

    private final Collection<Predicate<T>> predicates;
    private final Collection<Index<T, ?>> indices;

    public ExtendedListDataStream(Collection<Predicate<T>> predicates, Collection<Index<T, ?>> indices) {
        this.predicates = predicates;
        this.indices = indices;
    }

    public DataHandle<T> append(T t) {
        DataHandle<T> dh = DataHandle.of(t);
        store.put(t, dh);
        order.add(dh);
        subscribers.forEach(s -> notifySubscriber(s, dh));
        return dh;
    }

    public void subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
        order.forEach(dh -> notifySubscriber(subscriber, dh));
    }

    public void subscribe(Subscriber<T> subscriber, int indexId) {
        subscribers.add(subscriber);
        order.forEach(dh -> notifySubscriber(subscriber, dh));
    }

    private void notifySubscriber(Subscriber<T> subscriber, DataHandle<T> dh) {
        if (predicates.stream().anyMatch(p -> p.test(dh.getObject()))) {
            subscriber.added(dh);
        }
    }
}

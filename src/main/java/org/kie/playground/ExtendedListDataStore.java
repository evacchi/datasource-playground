package org.kie.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class ExtendedListDataStore<T> implements DataStore<T> {

    private final IdentityHashMap<Object, DataHandle<T>> store = new IdentityHashMap<>();
    private final ArrayList<DataHandle<T>> order = new ArrayList<>();
    private final HashMap<Long, Subscriber<T>> subscribers = new HashMap<>();

    private final Collection<Predicate<T>> predicates;
    private final Collection<Index<T, ?>> indices;

    public ExtendedListDataStore(Collection<Predicate<T>> predicates, Collection<Index<T, ?>> indices) {
        this.predicates = predicates;
        this.indices = indices;
    }

    public DataHandle<T> add(T t) {
        DataHandle<T> dh = DataHandle.of(t);
        store.put(t, dh);
        order.add(dh);

        indices.stream()
                .map(idx -> idx.apply(t))
                .map(subscribers::get)
                .forEach(s -> this.notifySubscriber(s, dh));
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

    @Override
    public void remove(T object) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}

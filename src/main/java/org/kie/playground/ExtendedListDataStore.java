package org.kie.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;

public class ExtendedListDataStore<T> implements DataStore<T> {

    private final IdentityHashMap<Object, DataHandle<T>> store = new IdentityHashMap<>();
    private final ArrayList<DataHandle<T>> order = new ArrayList<>();

    private final Collection<Subscriber<T>> subscribers;
    private final FanOut<T> fanOut;

    public ExtendedListDataStore() {
        this.fanOut = new FanOut<T>();    // this is the "no-index" index
        this.subscribers = new ArrayList<>(); // other indices
    }

    public ExtendedListDataStore(Collection<Index<T, ?>> indices) {
        this();
        this.subscribers.addAll(indices);
    }

    public DataHandle<T> add(T t) {
        DataHandle<T> dh = DataHandle.of(t);
        store.put(t, dh);
        order.add(dh);
        // propagate to everyone (hash/filter is delegated to impl)
        fanOut.added(dh);
        subscribers.forEach(idx -> idx.added(dh));
        return dh;
    }

    public void subscribe(Subscriber<T> subscriber) {
        fanOut.subscribe(subscriber);
    }

    @Override
    public void remove(T object) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}

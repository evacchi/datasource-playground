package org.kie.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.function.Predicate;

public class ExtendedListDataStore<T> implements DataStore<T> {
    private final IdentityHashMap<Object, DataHandle<T>> store = new IdentityHashMap<>();
    private final ArrayList<DataHandle<T>> order = new ArrayList<>();
    private final ArrayList<Subscriber<T>> subscribers = new ArrayList<>();

    private final Collection<Predicate<T>> predicates;
    private final Collection<Index> indices;

    public ExtendedListDataStore(Collection<Predicate<T>> predicates, Collection<Index> indices) {
        this.predicates = predicates;
        this.indices = indices;
    }

    public DataHandle<T> add(T t) {
        DataHandle<T> dh = DataHandle.of(t);
        store.put(t, dh);
        order.add(dh);
        subscribers.forEach(s -> s.added(dh));
        return dh;
    }

    @Override
    public void remove(T object) {
        remove(store.get(object));
    }

    public void remove(DataHandle<T> handle) {
        store.remove(handle.getObject());
        order.remove(handle);
        subscribers.forEach(s -> s.removed(handle));
    }

    public void subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
        order.forEach(subscriber::added);
    }

}

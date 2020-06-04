package org.kie.playground;

import java.util.ArrayList;
import java.util.IdentityHashMap;

public class ListDataStore<T> implements DataStore<T> {
    private final IdentityHashMap<Object, DataHandle<?>> store = new IdentityHashMap<>();
    private final ArrayList<Subscriber<T>> subscribers = new ArrayList<>();

    public DataHandle<T> add(T t) {
        DataHandle<T> dh = DataHandle.of(t);
        store.put(t, dh);
        subscribers.forEach(s -> s.added(dh));
        return dh;
    }

    @Override
    public void remove(T object) {
        remove((DataHandle<T>) store.get(object));
    }

    public void remove(DataHandle<T> handle) {
        store.remove(handle.getObject());
        subscribers.forEach(s -> s.removed(handle));
    }

    public void subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
    }

}

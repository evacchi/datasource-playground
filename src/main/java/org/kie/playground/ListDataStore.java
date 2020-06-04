package org.kie.playground;

import java.util.ArrayList;
import java.util.IdentityHashMap;

public class ListDataStore<T> implements DataStore<T> {
    private final IdentityHashMap<Object, DataHandle<T>> store = new IdentityHashMap<>();
    private final ArrayList<DataHandle<T>> order = new ArrayList<>();
    private final ArrayList<Subscriber<T>> subscribers = new ArrayList<>();

    public static <T> DataStore<T> create() {
        return new ListDataStore<>();
    }

    ListDataStore() {}

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

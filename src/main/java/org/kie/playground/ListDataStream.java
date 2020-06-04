package org.kie.playground;

import java.util.ArrayList;
import java.util.IdentityHashMap;

public class ListDataStream<T> implements DataStream<T> {
    private final IdentityHashMap<Object, DataHandle<T>> store = new IdentityHashMap<>();
    private final ArrayList<DataHandle<T>> order = new ArrayList<>();
    private final ArrayList<Subscriber<T>> subscribers = new ArrayList<>();

    public static <T> DataStream<T> create() {
        return new ListDataStream<>();
    }

    ListDataStream() {}

    public DataHandle<T> append(T t) {
        DataHandle<T> dh = DataHandle.of(t);
        store.put(t, dh);
        order.add(dh);
        subscribers.forEach(s -> s.added(dh));
        return dh;
    }

    public void subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
        order.forEach(subscriber::added);
    }

}

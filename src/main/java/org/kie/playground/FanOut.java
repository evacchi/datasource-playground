package org.kie.playground;

import java.util.ArrayList;

public class FanOut<T> implements Subscriber<T> {

    ArrayList<Subscriber<T>> subscribers = new ArrayList<>();

    @Override
    public void added(DataHandle<T> dh) {
        subscribers.forEach(s -> s.added(dh));
    }

    @Override
    public void removed(DataHandle<T> dh) {
        subscribers.forEach(s -> s.removed(dh));
    }
}

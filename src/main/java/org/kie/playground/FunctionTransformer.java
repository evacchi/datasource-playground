package org.kie.playground;

import java.util.ArrayList;
import java.util.function.Function;

public class FunctionTransformer<T, R> implements Transformer<T, R> {
    private final ArrayList<Subscriber<R>> subscribers = new ArrayList<>();
    private final Function<T, R> mapper;

    public static <T, R> FunctionTransformer<T, R> of(Function<T, R> mapper) {
        return new FunctionTransformer<>(mapper);
    }

    FunctionTransformer(Function<T, R> mapper) {
        this.mapper = mapper;
    }

    public void subscribe(Subscriber<R> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void added(DataHandle<T> dh) {
        subscribers.forEach(s -> applyTransform(dh, s));
    }

    private void applyTransform(DataHandle<T> dh, Subscriber<R> s) {
        var mapped = mapper.apply(dh.getObject());
        var mappedDH = DataHandle.of(mapped);
        s.added(mappedDH);
    }

    @Override
    public void removed(DataHandle<T> dh) {
        // ?
    }
}

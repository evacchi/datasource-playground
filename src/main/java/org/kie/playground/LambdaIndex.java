package org.kie.playground;

import java.util.LinkedHashMap;
import java.util.function.Function;

public class LambdaIndex<T, R> implements Index<T, R> {

    private final Function<T, R> extractor;
    private final FanOut<T> fanOut = new FanOut<>();
    private final LinkedHashMap<R, Intermediate<T>> index = new LinkedHashMap<>();

    public static <T, R> LambdaIndex<T, R> of(Function<T, R> f) {
        return new LambdaIndex<>(f);
    }

    private LambdaIndex(Function<T, R> extractor) {
        this.extractor = extractor;
    }

    @Override
    public void added(DataHandle<T> dh) {
        var key = extractor.apply(dh.getObject());
        fanOut.added(dh);
        var intermediate = index.get(key);
        if (intermediate != null) {
            intermediate.added(dh);
        }
    }

    @Override
    public void removed(DataHandle<T> dh) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void subscribe(Subscriber<T> subscriber) {
        fanOut.subscribe(subscriber);
    }

    @Override
    public void subscribe(R dataExample, Subscriber<T> subscriber) {
        index.computeIfAbsent(dataExample, k -> new FanOut<>())
                .subscribe(subscriber);
    }
}

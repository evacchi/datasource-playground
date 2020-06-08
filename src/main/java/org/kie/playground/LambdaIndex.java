package org.kie.playground;

import java.util.LinkedHashMap;
import java.util.function.Function;

public class LambdaIndex<T, R> implements Index<T, R> {
    private final Function<T, R> extractor;
    private final LinkedHashMap<R, Subscriber<T>> index = new LinkedHashMap<>();

    public LambdaIndex(Function<T, R> extractor) {
        this.extractor = extractor;
    }

    @Override
    public void add(DataHandle<T> dh) {
        var key = extractor.apply(dh.getObject());
        var tSubscriber = index.get(key);
        tSubscriber.added(dh);
    }

}

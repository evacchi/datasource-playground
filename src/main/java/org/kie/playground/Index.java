package org.kie.playground;

public interface Index<T, R> extends Intermediate<T> {
    void subscribe(R dataExample, Subscriber<T> subscriber);
}

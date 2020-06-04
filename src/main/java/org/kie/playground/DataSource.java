package org.kie.playground;

public interface DataSource<T> {
    public void subscribe(Subscriber<T> subscriber);
}
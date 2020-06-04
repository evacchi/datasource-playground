package org.kie.playground;

public interface DataStore<T> extends DataSource<T> {

    DataHandle<T> add(T object);

    void remove(T object);
}

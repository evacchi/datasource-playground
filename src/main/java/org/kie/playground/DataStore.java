package org.kie.playground;

public interface DataStore<T> {

    DataHandle<T> add(T object);

    void remove(T object);
}

package org.kie.playground;

public interface DataStream<T> extends DataSource<T> {

    DataHandle<T> append(T object);

}

package org.kie.playground;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class DataHandle<T> {
    private static final AtomicLong counter = new AtomicLong();

    private final long id = counter.incrementAndGet();
    private final T object;

    static <T> DataHandle<T> of(T object) {
        return new DataHandle<>(object);
    }

    private DataHandle( T object ) {
        this.object = object;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        DataHandle<T> that = ( DataHandle<T> ) o;
        return id == that.id;
    }

    public T getObject() {
        return object;
    }

    public long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash( id );
    }


    @Override
    public String toString() {
        return "DataHandle{" +
                "id=" + id +
                ", object=" + object +
                '}';
    }}
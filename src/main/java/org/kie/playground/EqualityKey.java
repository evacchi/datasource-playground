package org.kie.playground;

import java.util.HashSet;
import java.util.Objects;

public class EqualityKey<T> {

    private final DataHandle<T> handle;
    private final HashSet<DataHandle<T>> stated = new HashSet<>();
    private final HashSet<DataHandle<T>> justified = new HashSet<>();

    public EqualityKey(DataHandle<T> handle) {
        this.handle = handle;
        this.state(handle);
    }

    public DataHandle<T> handle() {
        return handle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EqualityKey<?> that = (EqualityKey<?>) o;
        return handle.getObject().equals(that.handle.getObject());
    }

    @Override
    public int hashCode() {
        return handle.getObject().hashCode();
    }

    public boolean contains(DataHandle<T> value) {
        return stated.contains(value) || justified.contains(value);
    }

    public boolean isStated(DataHandle<T> value) {
        return stated.contains(value);
    }

    public boolean isJustified(DataHandle<T> value) {
        return justified.contains(value);
    }


    public void state(DataHandle<T> value) {
        if (isStated(value)) return;
        if (isJustified(value)) {
            this.justified.remove(value);
            this.stated.add(value);
        }
        this.stated.add(value);
    }

    public void justify(DataHandle<T> value) {
        if (isJustified(value)) return;
        if (isStated(value)) {
            this.justified.remove(value);
            this.stated.add(value);
        }

        this.justified.add(value);
    }

}

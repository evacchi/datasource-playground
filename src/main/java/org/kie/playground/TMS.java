package org.kie.playground;

import java.util.HashMap;
import java.util.function.Function;

public class TMS<T> {

    private HashMap<EqualityKey<T>, EqualityKey<T>> values = new HashMap<>();

    public DataHandle<T> state(T value) {
        var dh = DataHandle.of(value);
        var eqKey = values.computeIfAbsent(new EqualityKey<>(dh), Function.identity());
        eqKey.state(dh);
        return dh;
    }

    public DataHandle<T> justify(T value) {
        var dh = DataHandle.of(value);
        var eqKey = values.computeIfAbsent(new EqualityKey<>(dh), Function.identity());
        eqKey.justify(dh);
        return dh;
    }

}

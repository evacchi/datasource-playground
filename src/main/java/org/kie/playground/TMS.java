package org.kie.playground;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

public class TMS<T> {

    private final FanOut<T> stated;
    private final FanOut<T> justified;
    private HashMap<EqualityKey<T>, EqualityKey<T>> values = new HashMap<>();

    /*
    So the selector strategy is a simple one.
    1) For each Key
        1.1) if there is 1..n S propagate all of those and non of J.
        1.2) If 1..n J and no S, then propagate just one J.
     */

    public TMS() {
        this.stated = new FanOut<>() {
            @Override
            public void added(DataHandle<T> dh) {
                state(dh);
                // propagate
                super.added(dh);
            }
        };
        this.justified = new FanOut<>() {
            @Override
            public void added(DataHandle<T> dh) {
                var equalityKey = justify(dh);
                if (equalityKey.stated().isEmpty()) {
                    // propagate the equality representative
                    super.added(equalityKey.handle());
                }
            }
        };
    }

    public Subscriber<T> stated() {
        return stated;
    }

    private EqualityKey<T> state(DataHandle<T> dh) {
        var eqKey = values.computeIfAbsent(new EqualityKey<>(dh), Function.identity());
        eqKey.state(dh);
        return eqKey;
    }

    public Subscriber<T> justified() {
        return justified;
    }

    private EqualityKey<T> justify(DataHandle<T> dh) {
        var eqKey = values.computeIfAbsent(new EqualityKey<>(dh), Function.identity());
        eqKey.justify(dh);
        return eqKey;
    }
}

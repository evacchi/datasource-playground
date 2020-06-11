package org.kie.playground;

import java.util.HashMap;
import java.util.function.Function;

public class Belief<T> implements DataSource<T> {

    private final FanOut<T> stated;
    private final FanOut<T> justified;
    private final FanOut<T> fanOut;
    private final HashMap<EqualityKey<T>, EqualityKey<T>> values = new HashMap<>();

    /*
    Simple selector strategy
    1) For each Key
        1.1) if there is 1..n S propagate all of those and non of J.
        1.2) If 1..n J and no S, then propagate just one J.
     */

    public Belief() {
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
                    if (equalityKey.justified().size() == 1) {
                        // propagate this
                        super.added(dh);
                    } // otherwise do nothing
                }
            }
        };

        this.fanOut = new FanOut<>();
        this.stated.subscribe(fanOut);
        this.justified.subscribe(fanOut);
    }

    public void subscribe(Subscriber<T> subscriber) {
        this.fanOut.subscribe(subscriber);
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

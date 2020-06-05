package org.kie.playground;

import java.util.function.Function;
import java.util.function.Supplier;

public class LambdaIndex<T, U> implements Index<T, U> {
    private final Function<T, U> supplier;

    public LambdaIndex(Function<T, U> supplier) {
        this.supplier = supplier;
    }
}

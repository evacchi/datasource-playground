package org.kie.playground;

public class Unit<T> {
    public static <T> Unit<T> create(T context) {
        return new Unit<>(context);
    }

    private T context;
    
    Unit(T context) {
        this.context = context;
    }

    public T context() {
        return context;
    }

    public UnitInstance<T> createInstance() {
        return new UnitInstance<T>(this);
    }

}
package org.kie.playground;

import java.lang.reflect.Field;

public class UnitInstance<T> {

    Unit<T> unit;
    Runtime rt;

    UnitInstance(Unit<T> unit) {
        this.unit = unit;
        this.rt = new Runtime();
        bind();
    }

    private void bind() {
        var context = unit.context();
        for (Field f : context.getClass().getDeclaredFields()) {
            if (DataStore.class.isAssignableFrom(f.getType())) {
                try {
                    DataStore<Object> ds = (DataStore<Object>) f.get(context);
                    ds.subscribe(rt);
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
        }
    }

    public void run() {
        rt.run();
    }

    public Unit<T> unit() {
        return unit;
    }
}
package org.kie.playground;

public interface Subscriber<T> {
    public void added(DataHandle<T> dh);
    public void removed(DataHandle<T> dh);
}
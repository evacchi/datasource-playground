package org.kie.playground;

import java.util.ArrayList;
import java.util.List;

public class RecordingSubscriber<T> implements Subscriber<T> {

    ArrayList<T> list = new ArrayList<>();
    ArrayList<DataHandle<T>> handles = new ArrayList<>();


    @Override
    public void added(DataHandle<T> dh) {
        handles.add(dh);
        list.add(dh.getObject());
    }

    @Override
    public void removed(DataHandle<T> dh) {
        handles.remove(dh);
        list.remove(dh.getObject());
    }

    public List<T> getData() {
        return new ArrayList<>(list);
    }

    public List<DataHandle<T>> getHandles() {
        return handles;
    }
}
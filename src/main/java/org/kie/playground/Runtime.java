package org.kie.playground;

public class Runtime implements Subscriber<Object> {

    @Override
    public void added(DataHandle<Object> dh) {
        System.out.println("added:" + dh.getObject());
    }

    @Override
    public void removed(DataHandle<Object> dh) {
        System.out.println("removed:" + dh.getObject());
    }
    
}
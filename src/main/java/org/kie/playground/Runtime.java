package org.kie.playground;

import java.util.ArrayList;

public class Runtime implements Subscriber<Object> {

    ArrayList<Object> list = new ArrayList<>();

    @Override
    public void added(DataHandle<Object> dh) {
        list.add(dh.getObject());
    }

    @Override
    public void removed(DataHandle<Object> dh) {
        list.remove(dh.getObject());
    };

    public void run() {;
        System.out.println(list);
    }



    
}
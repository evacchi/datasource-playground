package org.kie.playground;

public interface Transformer<T, U> extends Subscriber<T>, DataSource<U> {

}

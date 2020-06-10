package org.kie.playground;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Join<Left, Right> implements DataSource<DHPair<Left, Right>> {

    private final FanOut<Left> left;
    private final FanOut<Right> right;
    private final RecordingSubscriber<Left> leftMemory;
    private final RecordingSubscriber<Right> rightMemory;
    private final BiPredicate<Left, Right> predicate;
    private final FanOut<DHPair<Left, Right>> subscribers;
    private final RecordingSubscriber<DHPair<Left, Right>> joinMemory;

    public static <Left, Right> Join<Left, Right> of(BiPredicate<Left, Right> predicate) {
        return new Join<>(predicate);
    }

    private Join(BiPredicate<Left, Right> predicate) {
        this.predicate = predicate;
        this.left = new FanOut<>();
        this.right = new FanOut<>();
        this.subscribers = new FanOut<>();
        this.leftMemory = new RecordingSubscriber<>() {
            @Override
            public void added(DataHandle<Left> left) {
                super.added(left);
                for (var right : rightMemory.getHandles()) {
                    if (predicate.test(left.getObject(), right.getObject())) {
                        subscribers.added(DataHandle.of(DHPair.of(left, right)));
                    }
                }
            }
        };
        this.rightMemory = new RecordingSubscriber<>() {
            public void added(DataHandle<Right> right) {
                super.added(right);
                for (var left : leftMemory.getHandles()) {
                    if (predicate.test(left.getObject(), right.getObject())) {
                        subscribers.added(DataHandle.of(DHPair.of(left, right)));
                    }
                }
            }
        };
        this.left.subscribe(leftMemory);
        this.right.subscribe(rightMemory);
        this.joinMemory = new RecordingSubscriber<>();
        this.subscribers.subscribe(joinMemory);
    }

    // return subscriber to add subscriptions
    public Subscriber<Left> left() {
        return left;
    }

    public Subscriber<Right> right() {
        return right;
    }

    public void subscribe(Subscriber<DHPair<Left, Right>> subscriber) {
        this.subscribers.subscribe(subscriber);
    }

    public List<Pair<Left, Right>> getPairs() {
        return joinMemory.getData().stream().map(DHPair::toPair).collect(Collectors.toList());
    }
}

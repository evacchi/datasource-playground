package org.kie.playground;

import java.util.Objects;

public class Pair<Left, Right> {

    public final Left left;
    public final Right right;

    public static <Left, Right> Pair<Left, Right> of(Left left, Right right) {
        return new Pair<>(left, right);
    }

    public Pair(Left left, Right right) {
        this.left = left;
        this.right = right;
    }

    public Left left() {
        return left;
    }

    public Right right() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(left, pair.left) &&
                Objects.equals(right, pair.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Pair{");
        sb.append(left);
        sb.append(",").append(right);
        sb.append('}');
        return sb.toString();
    }
}


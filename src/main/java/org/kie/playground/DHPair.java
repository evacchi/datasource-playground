package org.kie.playground;

public final class DHPair<Left, Right> {

    public final DataHandle<Left> left;
    public final DataHandle<Right> right;

    public static <Left, Right> DHPair<Left, Right> of(DataHandle<Left> left, DataHandle<Right> right) {
        return new DHPair<>(left, right);
    }

    private DHPair(DataHandle<Left> left, DataHandle<Right> right) {
        this.left = left;
        this.right = right;
    }

    public DataHandle<Left> left() {
        return left;
    }

    public DataHandle<Right> right() {
        return right;
    }

    public Pair<Left, Right> toPair() {
        return Pair.of(left.getObject(), right.getObject());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Pair{");
        sb.append(left.getObject());
        sb.append(",").append(right.getObject());
        sb.append('}');
        return sb.toString();
    }
}


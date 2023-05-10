package subway.domain;

import java.util.Objects;

public class Connection {

    private final boolean left;
    private final boolean right;

    public Connection(final boolean left, final boolean right) {
        this.left = left;
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isDisConnected() {
        return !(left && right);
    }

    @Override
    public String toString() {
        return "Connection{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Connection that = (Connection) o;
        return left == that.left && right == that.right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}

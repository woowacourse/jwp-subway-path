package subway.domain;

import java.util.Objects;

public class Stations {

    private Station current;
    private Station next;
    private int distance;

    public Stations(final Station current, final Station next, final int distance) {
        this.current = current;
        this.next = next;
        this.distance = distance;
    }

    public Station getCurrent() {
        return current;
    }

    public Station getNext() {
        return next;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Stations{" +
                "current=" + current +
                ", next=" + next +
                ", distance=" + distance +
                '}';
    }
}

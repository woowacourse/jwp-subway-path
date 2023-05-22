package subway.domain.subway;

import subway.domain.station.Station;

public class Passenger {

    private final int age;
    private final Station start;
    private final Station end;

    public Passenger(final int age, final Station start, final Station end) {
        this.age = age;
        this.start = start;
        this.end = end;
    }

    public int getAge() {
        return age;
    }

    public Station getStart() {
        return start;
    }

    public Station getEnd() {
        return end;
    }
}

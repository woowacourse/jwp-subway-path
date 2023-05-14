package subway.domain;

public class Section {
    private static final int MIN_DISTANCE = 1;

    private final Station source;
    private final Station target;
    private final int distance;

    public Section(final String source, final String target, final int distance) {
        this(new Station(source), new Station(target), distance);
    }

    public Section(final Station source, final Station target, final int distance) {
        validate(source, target, distance);
        this.source = source;
        this.target = target;
        this.distance = distance;
    }

    private void validate(final Station source, final Station target, final int distance) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 동일할 수 없습니다.");
        }
        if (distance < MIN_DISTANCE) {
            throw new IllegalArgumentException(String.format("거리 정보는 %d 이상이어야 합니다.", MIN_DISTANCE));
        }
    }

    public boolean have(final Station station) {
        return source.equals(station) || target.equals(station);
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public int getDistance() {
        return distance;
    }
}

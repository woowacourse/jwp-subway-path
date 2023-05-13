package subway.domain;

import java.util.List;
import java.util.Objects;

public class LineDomain {

    private static final int NAME_LENGTH = 20;

    private final Long id;
    private final String name;
    private final String color;
    private final List<Station> stations;

    public LineDomain(final String name, final String color, final List<Station> stations) {
        this(null, name, color, stations);
    }

    public LineDomain(final Long id, final String name, final String color, final List<Station> stations) {
        validate(name, color, stations);
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    private void validate(final String name, final String color, final List<Station> stations) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("노선 이름을 null일 수 없습니다.");
        }
        if (name.isBlank() || name.length() > NAME_LENGTH) {
            throw new IllegalArgumentException("노선 이름의 길이는 1이상 20이하이어야 합니다.");
        }
        if (Objects.isNull(color)) {
            throw new IllegalArgumentException("노선 색상은 null일 수 없습니다.");
        }
        if (Objects.isNull(stations)) {
            throw new IllegalArgumentException("노선이 가지는 지하철은 null일 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return stations;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LineDomain that = (LineDomain) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(color, that.color) && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, stations);
    }
}

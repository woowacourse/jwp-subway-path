package subway.domain.subwaymap;

import java.util.Objects;

public class Station {

    private Long id;
    private String name;

    Station() {
    }

    private Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Station from(final String name) {
        validate(name);
        return new Station(null, name);
    }

    private static void validate(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("역의 이름은 비어있을 수 없습니다.");
        }
        if (name.length() > 10) {
            throw new IllegalArgumentException("역의 이름은 10자를 넘을 수 없습니다.");
        }
    }

    public static Station of(final Long id, final String name) {
        return new Station(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Station station = (Station) o;
        if (Objects.isNull(id) || Objects.isNull(station.id)) {
            return false;
        }
        return Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

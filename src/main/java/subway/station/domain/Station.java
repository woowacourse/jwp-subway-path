package subway.station.domain;

import subway.station.exception.NameLengthException;

import java.util.Objects;

public class Station {

    public static final int MINIMUM_NAME_LENGTH = 2;
    public static final int MAXIMUM_NAME_LENGTH = 15;

    private final Long id;
    private final String name;

    public Station(Long id, String name) {
        String stripped = name.strip();
        validateNameLength(stripped);
        this.id = id;
        this.name = stripped;
    }

    public Station(String name) {
        this(null, name);
    }

    private void validateNameLength(String name) {
        if (name.length() < MINIMUM_NAME_LENGTH || name.length() > MAXIMUM_NAME_LENGTH) {
            throw new NameLengthException("이름 길이는 " + MINIMUM_NAME_LENGTH + "자 이상 " + MAXIMUM_NAME_LENGTH + "자 이하입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        if (Objects.isNull(id) || Objects.isNull(station.id)) {
            return Objects.equals(name, station.name);
        }
        return Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        if (Objects.isNull(id)) {
            return Objects.hash(name);
        }
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

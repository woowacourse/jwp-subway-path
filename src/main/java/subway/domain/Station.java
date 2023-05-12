package subway.domain;

import java.util.Objects;

public class Station {

    private final String name;

    public Station(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("아름에는 빈 문자가 들어올 수 없습니다.");
        }
    }

    public boolean isSameName(Station other) {
        return this.name.equals(other.getName());
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Station{" +
                ", name='" + name + '\'' +
                '}';
    }
}

package subway.domain;

import subway.domain.vo.Name;

import java.util.Objects;

public class Station {

    private final Long id;
    private final Name name;

    public Station(final String name) {
        this(null, name);
    }

    public Station(final Long id, final String name) {
        validate(name);
        this.id = id;
        this.name = Name.from(name);
    }

    private void validate(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("지하철 역 이름은 null 또는 공백일 수 없습니다.");
        }
        if (name.length() > 10) {
            throw new IllegalArgumentException("이름은 1~10글자여야합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getNameValue() {
        return name.getValue();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

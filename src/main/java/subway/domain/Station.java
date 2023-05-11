package subway.domain;

import java.util.List;
import java.util.Objects;

public class Station {

    private Long id;
    private String name;
    private List<Section> sections;

    public Station(final String name, final List<Section> sections) {
        this(null, name, sections);
    }

    public Station(final Long id, final String name, final List<Section> sections) {
        validate(name);
        this.id = id;
        this.name = name;
        this.sections = sections;
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

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return sections;
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
        return Objects.equals(id, station.id) && Objects.equals(name, station.name) && Objects.equals(sections, station.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sections);
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sections=" + sections +
                '}';
    }
}

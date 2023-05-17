package subway.business.domain;

import java.util.Objects;

public class Station {
    private final Long id;
    private final Name name;
    private final Long sectionId;

    public Station(Long id, Name name, Long sectionId) {
        this.id = id;
        this.name = name;
        this.sectionId = sectionId;
    }

    public Station(Long id, String name) {
        this(id, new Name(name), null);
    }

    public Station(String name) {
        this(null, new Name(name), null);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public Long getSectionId() {
        return sectionId;
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
        return name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

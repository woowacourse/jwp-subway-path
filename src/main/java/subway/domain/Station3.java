package subway.domain;

import java.util.Objects;

public class Station3 {
    private Long id;
    private String name;

    public Station3() {
    }

    public Station3(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station3(String name) {
        this.name = name;
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
        Station3 station = (Station3) o;
        return id.equals(station.id) && name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

package subway.domain;

import java.util.Objects;

public class Station {
    private Long id;
    private String name;

    private Station() {
    }

    /**
     * insert를 할 떄
     * TODO 삭제하기 -> insert문 두 개로
     */
    public Station(Long id) {
        this(id, null);
    }

    public Station(String name) {
        this(null, name);
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return id.equals(station.id) && name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

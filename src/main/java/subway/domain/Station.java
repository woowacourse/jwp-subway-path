package subway.domain;

import java.util.Objects;

public class Station {
    private static final String INITIAL_VALUE = "기본역";
    private Long id;
    private Name name;

    private Station() {
    }

    /**
     * insert를 할 떄
     * TODO 삭제하기 -> insert문 두 개로
     */
    public Station(Long id) {
        this(id, INITIAL_VALUE);
    }

    public Station(String name) {
        this(null, name);
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = Name.from(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id) && Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }
}

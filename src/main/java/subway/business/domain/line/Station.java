package subway.business.domain.line;

import java.util.Objects;

public class Station {
    private final Long id;
    private final String name;

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Station from(String name) {
        return new Station(null, name);
    }

    public boolean hasNameOf(String name) {
        return this.name.equals(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this.id == null) {
            throw new IllegalStateException("ID가 존재하지 않는 Station을 기준으로 비교했습니다.");
        }
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        if (station.id == null) {
            throw new IllegalStateException("ID가 존재하지 않는 Station을 인자로 넣어 비교했습니다.");
        }
        return Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // TODO 제거하기
    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

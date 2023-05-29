package subway.domain.station;

import subway.exception.SubwayIllegalArgumentException;

public class Station {

    private static final int MAX_NAME_LENGTH = 50;

    private final Long id;
    private final String name;

    public Station(final String name) {
        this(null, name);
    }

    public Station(final Long id, final String name) {
        validateName(name);
        this.id = id;
        this.name = name.strip();
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new SubwayIllegalArgumentException("이름은 비어있을 수 없습니다.");
        }
        if (name.strip().length() > MAX_NAME_LENGTH) {
            throw new SubwayIllegalArgumentException("이름은 " + MAX_NAME_LENGTH + "자 이하여야합니다.");
        }
    }

    // TODO(질문): 도메인에서 id 만으로 equals 비교를 해도 되나?! (속성들 말고?)
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Station station = (Station) o;

        return id != null ? id.equals(station.id) : station.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

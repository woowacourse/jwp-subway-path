package subway.domain.line;

import java.util.Objects;
import subway.dao.entity.StationEntity;
import subway.exception.InvalidFormatException;

public class Station {

    private static final String BLANK = " ";

    private final Long id;
    private final String name;

    public Station(Long id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    public static Station from(StationEntity stationEntity) {
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    private void validateName(String name) {
        validateBlank(name);
        validateLength(name);
    }

    private void validateBlank(String name) {
        if (name.isBlank() || name.contains(BLANK)) {
            throw new InvalidFormatException("역 이름에는 공백이 허용되지 않습니다.");
        }
    }

    private void validateLength(String name) {
        if (name.length() < 2 || name.length() > 10) {
            throw new InvalidFormatException("역 이름은 2~10자까지 가능합니다.");
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(getId(), station.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

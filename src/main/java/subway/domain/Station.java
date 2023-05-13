package subway.domain;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Station {

    private static final Pattern PATTERN = Pattern.compile("^[가-힣0-9]*$");
    private static final int MINIMUM_LENGTH = 2;
    private static final int MAXIMUM_LENGTH = 9;

    private final Long id;
    private final String name;
    private final AdjustPath adjustPath;

    private Station(final Long id, final String name, final AdjustPath adjustPath) {
        validateName(name);

        this.id = id;
        this.name = name;
        this.adjustPath = adjustPath;
    }

    public static Station from(final String name) {
        return new Station(null, name, AdjustPath.create());
    }

    public static Station of(final String name) {
        return new Station(null, name, AdjustPath.create());
    }

    public static Station of(final Long id, final String name) {
        return new Station(id, name, AdjustPath.create());
    }

    public static Station of(final Station station) {
        return new Station(station.id, station.name, AdjustPath.create());
    }

    private void validateName(final String name) {
        validateFormat(name);
        validateLength(name);
    }

    private void validateLength(final String name) {
        if (!(MINIMUM_LENGTH <= name.length() && name.length() <= MAXIMUM_LENGTH)) {
            throw new IllegalArgumentException("역 이름은 2글자 ~ 9글자만 가능합니다.");
        }
    }

    private void validateFormat(final String name) {
        if (!PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("역 이름은 한글과 숫자만 가능합니다.");
        }
    }

    public void addPath(final Station station, final Distance distance, final Direction direction) {
        adjustPath.add(station, distance, direction);
    }

    public void deletePath(final Station station) {
        adjustPath.delete(station);
    }

    public Distance findDistanceByStation(final Station target) {
        return adjustPath.findPathInfoByStation(target)
                .getDistance();
    }

    public List<Station> findAdjustStation() {
        return adjustPath.findAllStation();
    }

    public AdjustPath getAdjustPath() {
        return adjustPath;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isConnect(final Station newDownStation) {
        return adjustPath.isConnect(newDownStation);
    }

    public Direction findEndStationPathDirection() {
        return adjustPath.findEndStationPathDirection();
    }

    public boolean isEnd() {
        return adjustPath.isEnd();
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
        return Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

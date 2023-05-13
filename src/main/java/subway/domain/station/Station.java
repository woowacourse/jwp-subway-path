package subway.domain.station;

import java.util.List;
import java.util.Objects;
import subway.domain.Direction;
import subway.domain.Distance;

public class Station {

    private final Long id;
    private final StationName name;
    private final AdjustPath adjustPath;

    private Station(final Long id, final String name, final AdjustPath adjustPath) {
        this.id = id;
        this.name = StationName.from(name);
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
        return new Station(station.id, station.name.getName(), AdjustPath.create());
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

    public Distance calculateMiddleDistance(final Station targetStation, final Distance distance) {
        final PathInfo pathInfo = adjustPath.findPathInfoByStation(targetStation);

        return pathInfo.calculateMiddleDistance(distance);
    }

    public List<Station> findAdjustStation() {
        return adjustPath.findAllStation();
    }

    public List<Station> findAllAdjustPathStation() {
        return adjustPath.findAllStation();
    }

    public Direction findEndStationPathDirection() {
        return adjustPath.findEndStationPathDirection();
    }

    public boolean isTerminalStation() {
        return adjustPath.isTerminalStation();
    }

    public Direction findDirectionByAdjustPathStation(final Station otherStation) {
        return adjustPath.findDirectionByStation(otherStation);
    }

    public Station findStationByDirection(final Direction direction) {
        return adjustPath.findStationByDirection(direction);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
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

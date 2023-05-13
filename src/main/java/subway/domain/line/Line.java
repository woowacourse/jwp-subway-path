package subway.domain.line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import subway.domain.AdjustPath;
import subway.domain.Direction;
import subway.domain.Distance;
import subway.domain.Station;

public class Line {

    private final Long id;
    private final LineName name;
    private final LineColor color;
    private final List<Station> stations;

    private Line(final Long id, final String name, final String color, final List<Station> stations) {
        this.id = id;
        this.name = LineName.from(name);
        this.color = LineColor.from(color);
        this.stations = stations;
    }

    public static Line of(final Long id, final String name, final String color) {
        return new Line(id, name, color, new ArrayList<>());
    }

    public static Line of(final String name, final String color) {
        return new Line(null, name, color, new ArrayList<>());
    }

    public void addInitialStations(final Station upStation, final Station downStation, final Distance distance) {
        validateAlreadyRegisterStation(upStation);
        validateAlreadyRegisterStation(downStation);

        upStation.addPath(downStation, distance, Direction.DOWN);
        downStation.addPath(upStation, distance, Direction.UP);

        stations.add(upStation);
        stations.add(downStation);
    }

    public void addEndStation(final Station sourceStation, final Station targetStation, final Distance distance) {
        validateAlreadyRegisterStation(targetStation);

        final Station findSourceStation = findStation(sourceStation);
        final Direction direction = findSourceStation.findEndStationPathDirection();

        findSourceStation.addPath(targetStation, distance, direction.reverse());
        targetStation.addPath(findSourceStation, distance, direction);
        stations.add(targetStation);
    }

    public void addMiddleStation(final Station upStation,
            final Station downStation,
            final Station targetStation,
            final Distance distance) {
        validateAlreadyRegisterStation(targetStation);

        final Station findUpStation = findStation(upStation);
        final Station findDownStation = findStation(downStation);

        processAddMiddleStation(findUpStation, findDownStation, targetStation, distance);
        stations.add(targetStation);
    }

    private void processAddMiddleStation(
            final Station upStation,
            final Station downStation,
            final Station targetStation,
            final Distance distance) {
        final Distance middleDistance = upStation.calculateMiddleDistance(downStation, distance);

        upStation.addPath(targetStation, distance, Direction.DOWN);
        targetStation.addPath(upStation, distance, Direction.UP);
        downStation.addPath(targetStation, middleDistance, Direction.UP);
        targetStation.addPath(downStation, middleDistance, Direction.DOWN);
        upStation.deletePath(downStation);
        downStation.deletePath(upStation);
    }

    private void validateAlreadyRegisterStation(final Station targetStation) {
        if (stations.contains(targetStation)) {
            throw new IllegalArgumentException("이미 해당 노선에 등록된 역 입니다.");
        }
    }

    public void removeAllStation() {
        if (stations.size() != 2) {
            throw new IllegalArgumentException("역이 3개 이상 존재합니다.");
        }
        stations.clear();
    }

    public void removeEndStation(final Station targetStation) {
        final Station findTargetStation = findStation(targetStation);

        if (!findTargetStation.isEnd()) {
            throw new IllegalArgumentException("삭제하려는 역이 종점역이 아닙니다");
        }

        final List<Station> adjustStation = findTargetStation.findAdjustStation();
        for (Station station : adjustStation) {
            final Station newStation = findStation(station);
            newStation.deletePath(findTargetStation);
        }
        stations.remove(findTargetStation);
    }

    public void removeMiddleStation(final Station targetStation) {
        final Station findTargetStation = findStation(targetStation);
        final AdjustPath adjustPath = findTargetStation.getAdjustPath();
        final Station upStation = adjustPath.findStationByDirection(Direction.UP);
        final Station downStation = adjustPath.findStationByDirection(Direction.DOWN);

        extracted(findTargetStation, upStation, downStation);
    }

    private void extracted(final Station findTargetStation, final Station upStation, final Station downStation) {
        final Distance upStationDistance = upStation.findDistanceByStation(findTargetStation);
        final Distance downStationDistance = downStation.findDistanceByStation(findTargetStation);
        final Distance distance = upStationDistance.add(downStationDistance);

        upStation.deletePath(findTargetStation);
        downStation.deletePath(findTargetStation);
        upStation.addPath(downStation, distance, Direction.DOWN);
        downStation.addPath(upStation, distance, Direction.UP);
        stations.remove(findTargetStation);
    }

    private Station findStation(final Station target) {
        return stations.stream()
                .filter(station -> station.equals(target))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("노선에 등록되지 않은 역입니다"));
    }

    public List<Station> findStationsByOrdered() {
        if (this.stations.isEmpty()) {
            return Collections.emptyList();
        }

        final Queue<Station> queue = new LinkedList<>();
        final Set<Station> visited = new LinkedHashSet<>();

        final Station upStation = findStartStation();
        queue.add(upStation);
        visited.add(upStation);

        while (!queue.isEmpty()) {
            final Station nowStation = queue.poll();
            for (final Station nextStation : nowStation.findAdjustStation()) {
                if (!visited.contains(nextStation)) {
                    queue.add(nextStation);
                    visited.add(nextStation);
                }
            }
        }

        return new ArrayList<>(visited);
    }

    private Station findStartStation() {
        return stations.stream()
                .filter(Station::isEnd)
                .filter(station -> station.findEndStationPathDirection().matches(Direction.DOWN))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("아직 노선에 역이 등록되지 않았습니다."));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getColor() {
        return color.getColor();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

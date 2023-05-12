package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

public class Line {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[가-힣0-9]*$");
    private static final Pattern COLOR_PATTERN = Pattern.compile("^bg-[a-z]{3,7}-\\d{2,3}$");
    private static final int MINIMUM_LENGTH = 2;
    private static final int MAXIMUM_LENGTH = 9;

    private final Long id;
    private final String name;
    private final String color;
    private final List<Station> stations;

    private Line(final Long id, final String name, final String color, final List<Station> stations) {
        validateLineInfo(name, color);

        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static Line of(final Long id, final String name, final String color) {
        return new Line(id, name, color, new ArrayList<>());
    }

    public static Line of(final String name, final String color) {
        return new Line(null, name, color, new ArrayList<>());
    }

    private void validateLineInfo(final String name, final String color) {
        validateNameFormat(name);
        validateNameLength(name);
        validateColorFormat(color);
    }

    private void validateNameFormat(final String name) {
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("노선 이름은 한글과 숫자만 가능합니다");
        }
    }

    private void validateNameLength(final String name) {
        if (!(MINIMUM_LENGTH <= name.length() && name.length() <= MAXIMUM_LENGTH)) {
            throw new IllegalArgumentException("노선 이름은 2글자 ~ 9글자만 가능합니다");
        }
    }

    private void validateColorFormat(final String color) {
        if (!COLOR_PATTERN.matcher(color).matches()) {
            throw new IllegalArgumentException("노선 색깔은 tailwindcss 형식만 가능합니다");
        }
    }

    public void addInitialStations(final Station upStation, final Station downStation, final Distance distance) {
        validateStation(upStation);
        validateStation(downStation);
        final Station newUpStation = Station.of(upStation);
        final Station newDownStation = Station.of(downStation);

        newUpStation.addPath(newDownStation, distance, Direction.DOWN);
        newDownStation.addPath(newUpStation, distance, Direction.UP);

        stations.add(newUpStation);
        stations.add(newDownStation);
    }

    public Station findStation(final Station target) {
        return stations.stream()
                .filter(station -> station.equals(target))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("노선에 등록되지 않은 역입니다"));
    }

    public void addEndStation(final Station sourceStation, final Station targetStation, final Distance distance) {
        validateStation(targetStation);

        final Station newSourceStation = findStation(sourceStation);
        final Direction direction = newSourceStation.findEndStationPathDirection();

        newSourceStation.addPath(targetStation, distance, direction.reverse());
        targetStation.addPath(newSourceStation, distance, direction);
        stations.add(targetStation);
    }

    public void addMiddleStation(final Station upStation,
                                 final Station downStation,
                                 final Station targetStation,
                                 final Distance distance) {
        validateStation(targetStation);
        final Station newUpStation = findStation(upStation);
        final Station newDownStation = findStation(downStation);

        if (!newUpStation.isConnect(newDownStation)) {
            throw new IllegalArgumentException("역이 서로 연결되어 있지 않습니다.");
        }

        final Distance originDistance = newUpStation.findDistanceByStation(newDownStation);
        if (distance.isGreaterThanOrEqualTo(originDistance)) {
            throw new IllegalArgumentException("등록되는 역 중간에 다른 역이 존재합니다.");
        }

        newUpStation.deletePath(newDownStation);
        newDownStation.deletePath(newUpStation);
        newUpStation.addPath(targetStation, distance, Direction.DOWN);
        targetStation.addPath(newUpStation, distance, Direction.UP);
        newDownStation.addPath(targetStation, originDistance.minus(distance), Direction.UP);
        targetStation.addPath(newDownStation, originDistance.minus(distance), Direction.DOWN);
        stations.add(targetStation);
    }

    public void removeAllStation() {
        stations.clear();
    }

    public void removeEndStation(final Station targetStation) {
        final Station newTargetStation = findStation(targetStation);

        if (!newTargetStation.isEnd()) {
            throw new IllegalArgumentException("삭제하려는 역이 종점역이 아닙니다");
        }

        final List<Station> adjustStation = newTargetStation.findAdjustStation();
        for (Station station : adjustStation) {
            final Station newStation = findStation(station);
            newStation.deletePath(newTargetStation);
        }
        stations.remove(newTargetStation);
    }

    public void removeMiddleStation(final Station targetStation) {
        final Station newTargetStation = findStation(targetStation);
        final AdjustPath adjustPath = newTargetStation.getAdjustPath();

        final Station upStation = adjustPath.findStationByDirection(Direction.UP);
        final Station downStation = adjustPath.findStationByDirection(Direction.DOWN);
        final Distance upStationDistance = upStation.findDistanceByStation(newTargetStation);
        final Distance downStationDistance = downStation.findDistanceByStation(newTargetStation);
        final Distance distance = upStationDistance.add(downStationDistance);

        upStation.addPath(downStation, distance, Direction.DOWN);
        downStation.addPath(upStation, distance, Direction.UP);
        upStation.deletePath(targetStation);
        downStation.deletePath(targetStation);
        stations.remove(newTargetStation);
    }

    private void validateStation(final Station targetStation) {
        if (stations.contains(targetStation)) {
            throw new IllegalArgumentException("이미 해당 노선에 등록된 역 입니다.");
        }
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
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Line line = (Line) o;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package subway.domain;

import java.util.ArrayList;
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
        validate(name, color);

        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    private Line(final String name, final String color, final List<Station> stations) {
        this(null, name, color, stations);
    }

    public static Line of(final Long id, final String name, final String color) {
        return new Line(id, name, color, new ArrayList<>());
    }

    public static Line of(final String name, final String color) {
        return new Line(name, color, new ArrayList<>());
    }

    private void validate(final String name, final String color) {
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

    public void initialStations(final Station upStation, final Station downStation, final Distance distance) {
        final Station newUpStation = Station.of(upStation.getName(), StationStatus.UP);
        final Station newDownStation = Station.of(downStation.getName(), StationStatus.DOWN);

        newUpStation.addPath(newDownStation, distance);
        newDownStation.addPath(newUpStation, distance);

        stations.add(newUpStation);
        stations.add(newDownStation);
    }

    public Station findStation(final Station target) {
        for (Station station : stations) {
            if (station.equals(target)) {
                return station;
            }
        }

        throw new IllegalArgumentException("노선에 등록되지 않은 역입니다");
    }

    public void addEndStation(final Station sourceStation, final Station targetStation, final Distance distance) {
        final Station newSourceStation = findStation(sourceStation);

        if (!(newSourceStation.isEnd(StationStatus.UP) || newSourceStation.isEnd(StationStatus.DOWN))) {
            throw new IllegalArgumentException("해당 역은 종점역이 아닙니다");
        }

        newSourceStation.addPath(targetStation, distance);
        targetStation.addPath(newSourceStation, distance);
        targetStation.changeEndStatus(newSourceStation);
        stations.add(targetStation);
    }

    public void addMiddleStation(final Station upStation, final Station downStation, final Station target, final Distance distance) {
        final Station newUpStation = findStation(upStation);
        final Station newDownStation = findStation(downStation);

        if (!newUpStation.isConnect(newDownStation)) {
            throw new IllegalArgumentException("역이 서로 연결되어 있지 않습니다");
        }

        final Distance originDistance = newUpStation.findDistanceByStation(newDownStation);
        if (distance.isGreaterThanOrEqualTo(originDistance)) {
            throw new IllegalArgumentException("역 사이의 거리는 양수여야 합니다");
        }

        upStation.deletePath(downStation);
        downStation.deletePath(upStation);
        newUpStation.addPath(target, distance);
        target.addPath(newUpStation, distance);
        newDownStation.addPath(target, originDistance.minus(distance));
        target.addPath(newDownStation, originDistance.minus(distance));
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

    public List<Station> getAllStation() {
        final Queue<Station> queue = new LinkedList<>();
        final Set<Station> visited = new LinkedHashSet<>();

        final Station upStation = findUpStation();
        queue.add(upStation);
        visited.add(upStation);

        while(!queue.isEmpty()) {
            final Station nowStation = queue.poll();
            for (final Station nextStation: nowStation.findAdjustStation()) {
                if (!visited.contains(nextStation)) {
                    queue.add(nextStation);
                    visited.add(nextStation);
                }
            }
        }

        return new ArrayList<>(visited);
    }

    private Station findUpStation() {
        for (Station station : stations) {
            if (station.isEnd(StationStatus.UP)) {
                return station;
            }
        }

        throw new IllegalArgumentException("아직 노선이 생성되지 않았습니다");
    }
}

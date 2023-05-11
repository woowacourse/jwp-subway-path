package subway.domain;

import java.util.ArrayList;
import java.util.List;
import subway.exception.InvalidLineNameException;
import subway.exception.InvalidSectionException;

public class Subway {

    private final List<Line> lines;

    public Subway(final List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public void add(
            final String lineName,
            final String baseStationName,
            final String additionalStationName,
            final int distanceValue,
            final Direction direction
    ) {
        final Station base = new Station(baseStationName);
        final Station additional = new Station(additionalStationName);
        final Distance distance = new Distance(distanceValue);

        if (lines.stream().anyMatch(line -> line.containsAll(base, additional))) {
            throw new InvalidSectionException("지하철 전체 노선에 이미 존재하는 구간입니다.");
        }

        final Line findLine = lines.stream()
                .filter(line -> line.isSameName(lineName))
                .findFirst()
                .orElseThrow(InvalidLineNameException::new);

        findLine.add(base, additional, distance, direction);
    }

    public void remove(final String lineName, final String stationName) {
        final Line findLine = lines.stream()
                .filter(line -> line.isSameName(lineName))
                .findFirst()
                .orElseThrow(InvalidLineNameException::new);

        findLine.remove(new Station(stationName));
    }

    public void initialAdd(
            final String lineName,
            final String leftStationName,
            final String rightStationName,
            final Integer distance
    ) {
        if (leftStationName.equals(rightStationName)) {
            throw new InvalidSectionException("동일한 이름을 가진 역을 구간에 추가할 수 없습니다.");
        }
        final Station left = new Station(leftStationName);
        final Station right = new Station(rightStationName);

        if (lines.stream().anyMatch(line -> line.containsAll(left, right))) {
            throw new InvalidSectionException("지하철 전체 노선에 이미 존재하는 구간입니다.");
        }

        final Line findLine = lines.stream()
                .filter(line -> line.isSameName(lineName))
                .findFirst()
                .orElseThrow(InvalidLineNameException::new);

        findLine.initialAdd(new Section(left, right, new Distance((distance))));
    }

    public List<Line> getLines() {
        return lines;
    }
}

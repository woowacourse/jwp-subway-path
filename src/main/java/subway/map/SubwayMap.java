package subway.map;

import java.util.ArrayList;
import java.util.List;
import subway.line.domain.SubwayLine;

public class SubwayMap {

    private final List<SubwayLine> lines = new ArrayList<>();

    public void addLine(final String lineName) {
        SubwayLine.register(lineName);
        lines.add(SubwayLine.get(lineName));
    }

    public void removeLine(final String lineName) {
        lines.remove(SubwayLine.get(lineName));
        SubwayLine.remove(lineName);
    }

    public void initializeLine(
        final String lineName,
        final String upStationName,
        final String downStationName,
        final int weight
    ) {
        validateExistLine(lineName);
        SubwayLine.get(lineName)
            .initializeLine(upStationName, downStationName, weight);
    }

    private void validateExistLine(final String lineName) {
        if (!lines.contains(SubwayLine.get(lineName))) {
            throw new IllegalArgumentException("해당 이름의 노선이 존재하지 않습니다.");
        }
    }

    public void addStationInLine(
        final String lineName,
        final String upStationName,
        final String downStationName,
        final int weight
    ) {
        validateExistLine(lineName);
        SubwayLine.get(lineName)
            .addStation(upStationName, downStationName, weight);
    }

    private void removeStationInLine(final String lineName, final String stationName) {
        validateExistLine(lineName);
        SubwayLine.get(lineName)
            .removeStation(stationName);
    }
}

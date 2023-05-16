package subway.domain.subway;

import java.util.*;

public class Lines {

    private final List<Line> lines;

    public Lines(final List<Line> lines) {
        this.lines = lines;
    }

    public Map<String, Station> getStationsFromNameMap() {
        Map<String, Station> stationWithId = new LinkedHashMap<>();
        lines.forEach(line -> fillStationWithIdMap(stationWithId, line));
        return stationWithId;
    }

    private void fillStationWithIdMap(final Map<String, Station> stationWithId, final Line line) {
        for (Section section : line.getSections()) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            stationWithId.put(upStation.getName(), upStation);
            stationWithId.put(downStation.getName(), downStation);
        }
    }

    public Set<String> getLinesNameFromStation(final Station station) {
        Set<String> lineNamesFromStation = new LinkedHashSet<>();

        for (Line line : lines) {
            for (Section section : line.getSections()) {
                if (section.hasStation(station)) {
                    lineNamesFromStation.add(line.getName());
                }
            }
        }

        return lineNamesFromStation;
    }

    public List<Line> getLines() {
        return lines;
    }
}

package subway.domain.subway;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        lines.forEach(line -> insertLineName(lineNamesFromStation, line, station));

        return lineNamesFromStation;
    }

    private void insertLineName(final Set<String> lineNamesFromStation, final Line line, final Station station) {
        line.getSections().stream()
                .filter(section -> section.hasStation(station))
                .map(section -> line.getName())
                .forEach(lineNamesFromStation::add);
    }

    public List<Line> getLines() {
        return lines;
    }
}

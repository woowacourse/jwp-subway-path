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

    public Map<String, Station> getStationsByNameInfo() {
        Map<String, Station> StationFromName = new LinkedHashMap<>();
        lines.forEach(line -> fillStationsFromLine(StationFromName, line));
        return StationFromName;
    }

    private void fillStationsFromLine(final Map<String, Station> stations, final Line line) {
        for (Section section : line.getSections()) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();

            stations.put(upStation.getName(), upStation);
            stations.put(downStation.getName(), downStation);
        }
    }

    public Set<String> getLineNamesFromStation(final Station station) {
        Set<String> lineNames = new LinkedHashSet<>();

        lines.forEach(line -> insertLineNames(lineNames, line, station));

        return lineNames;
    }

    private void insertLineNames(final Set<String> lineNamesFromStation, final Line line, final Station station) {
        line.getSections().stream()
                .filter(section -> section.hasStation(station))
                .map(section -> line.getName())
                .forEach(lineNamesFromStation::add);
    }

    public boolean isEmptyLines() {
        return lines.size() == 0;
    }

    public List<Line> getLines() {
        return lines;
    }
}

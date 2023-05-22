package subway.domain.subway;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Lines {

	private static final int EMPTY_LINE_SIZE = 0;
	private final List<Line> lines;

	public Lines(final List<Line> lines) {
		this.lines = lines;
	}

	public Map<String, Station> getStationByNameKey() {
		Map<String, Station> stationByName = new LinkedHashMap<>();
		for (Line line : lines) {
			fillAllStationName(stationByName, line);
		}

		return stationByName;
	}

	private void fillAllStationName(final Map<String, Station> stations, final Line line) {
		for (Section section : line.getSections()) {
			Station upStation = section.getUpStation();
			Station downStation = section.getDownStation();

			stations.put(upStation.getName(), upStation);
			stations.put(downStation.getName(), downStation);
		}
	}

	public Set<String> getAllLineNames(final Station station) {
		Set<String> lineNames = new LinkedHashSet<>();
		for (Line line : lines) {
			findAndInsertLineName(lineNames, line, station);
		}

		return lineNames;
	}

	private void findAndInsertLineName(final Set<String> lineNames, final Line line, final Station station) {
		line.getSections().stream()
			.filter(section -> section.hasStation(station))
			.map(section -> line.getName())
			.forEach(lineNames::add);
	}

	public boolean isEmptyLines() {
		return lines.size() == EMPTY_LINE_SIZE;
	}

	public List<Line> getLines() {
		return lines;
	}
}

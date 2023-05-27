package subway.domain.subway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import subway.exception.UpStationNotFoundException;

public class Subway {

	private final Map<Station, List<Station>> subway;

	private Subway(final Map<Station, List<Station>> subway) {
		this.subway = subway;
	}

	public static Subway from(final Sections sections) {
		Map<Station, List<Station>> subway = new HashMap<>();

		sections.getSections()
			.forEach(section -> {
				Station upStation = section.getUpStation();
				Station downStation = section.getDownStation();

				subway.computeIfAbsent(upStation, station -> new ArrayList<>()).add(downStation);
				subway.computeIfAbsent(downStation, station -> new ArrayList<>()).add(upStation);
			});

		return new Subway(subway);
	}

	public List<Station> getSortedStations(final Sections sections) {
		List<Station> endPoints = getEndPoints();
		Station upEndPoint = getUpStationEndPoint(sections, endPoints);

		return bfs(upEndPoint);
	}

	private List<Station> getEndPoints() {
		return subway.keySet().stream()
			.filter(key -> subway.get(key).size() == 1)
			.collect(Collectors.toList());
	}

	private Station getUpStationEndPoint(final Sections sections, final List<Station> endPointStations) {
		return sections.getSections().stream()
			.flatMap(section -> endPointStations.stream()
				.filter(station -> station.equals(section.getUpStation())))
			.findFirst()
			.orElseThrow(UpStationNotFoundException::new);
	}

	private List<Station> bfs(final Station upEndPoint) {
		Map<Station, Boolean> visited = initVisited();
		List<Station> stations = new ArrayList<>();

		Queue<Station> queue = new LinkedList<>();
		queue.add(upEndPoint);

		while (!queue.isEmpty()) {
			Station station = queue.poll();
			stations.add(station);
			visited.put(station, true);
			addNextStation(visited, queue, station);
		}
		return stations;
	}

	private Map<Station, Boolean> initVisited() {
		Map<Station, Boolean> visited = new HashMap<>();
		for (Station station : subway.keySet()) {
			visited.put(station, false);
		}
		return visited;
	}

	private void addNextStation(final Map<Station, Boolean> visited, final Queue<Station> queue,
		final Station station) {
		for (Station nextStation : subway.get(station)) {
			addNotVisitedStation(visited, queue, nextStation);
		}
	}

	private void addNotVisitedStation(final Map<Station, Boolean> visited, final Queue<Station> queue,
		final Station nextStation) {
		if (!visited.get(nextStation)) {
			queue.add(nextStation);
		}
	}
}

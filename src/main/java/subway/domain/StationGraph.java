package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StationGraph {

    private final Map<Station, List<Section>> sectionsByStation;

    private StationGraph(final Map<Station, List<Section>> sectionsByStation) {
        this.sectionsByStation = sectionsByStation;
    }

    public static StationGraph of(final List<Section> sections) {
        final StationGraph stationGraph = new StationGraph(new HashMap<>());

        for (final Section section : sections) {
            stationGraph.put(section.getUpStation(), section);
            stationGraph.put(section.getDownStation(), section);
        }

        return stationGraph;
    }

    private void put(final Station station, final Section section) {
        sectionsByStation.computeIfAbsent(station, key -> new ArrayList<>()).add(section);
    }

    public List<Station> findStations(final Section section) {
        final Long lineId = section.getLineId();
        final List<Station> upStations = findStationsByDirection(section, Direction.UP, lineId);
        final List<Station> downStations = findStationsByDirection(section, Direction.DOWN, lineId);

        return merge(upStations, downStations);
    }

    private List<Station> findStationsByDirection(final Section section, final Direction direction, final Long lineId) {
        final Set<Station> visitedStations = new HashSet<>();
        visitedStations.add(getStationByDirection(section, direction.getOpposite()));
        final List<Station> result = new ArrayList<>();
        dfs(getStationByDirection(section, direction), visitedStations, result, lineId);
        return result;
    }

    private Station getStationByDirection(final Section section, final Direction direction) {
        if (direction == Direction.UP) {
            return section.getUpStation();
        }
        if (direction == Direction.DOWN) {
            return section.getDownStation();
        }
        throw new IllegalArgumentException("존재하지 않는 방향입니다.");
    }

    private void dfs(
            final Station station,
            final Set<Station> visitedStations,
            final List<Station> result,
            final Long lineId
    ) {
        if (visitedStations.contains(station)) {
            return;
        }
        result.add(station);
        visitedStations.add(station);

        final List<Section> sections = sectionsByStation.get(station);
        for (final Section section : sections) {
            final Station destination = getDestination(station, section);
            if (lineId.equals(section.getLineId())) {
                dfs(destination, visitedStations, result, lineId);
            }
        }
    }

    private Station getDestination(final Station currentStation, final Section section) {
        if (section.getUpStation().equals(currentStation)) {
            return section.getDownStation();
        }
        return section.getUpStation();
    }

    private List<Station> merge(final List<Station> upStations, final List<Station> downStations) {
        Collections.reverse(upStations);

        final List<Station> stations = new ArrayList<>();
        stations.addAll(upStations);
        stations.addAll(downStations);

        return stations;
    }
}

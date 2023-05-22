package subway.domain.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.Stations;

public class StationGraph {

    private final Map<Station, Sections> sectionsByStation;

    private StationGraph(final Map<Station, Sections> sectionsByStation) {
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
        sectionsByStation.computeIfAbsent(station, key -> new Sections()).add(section);
    }

    public Stations findStations(final Section section) {
        final Long lineId = section.getLineId();
        final Stations upStations = findStationsByDirection(section, Direction.UP, lineId);
        final Stations downStations = findStationsByDirection(section, Direction.DOWN, lineId);

        return Stations.merge(upStations, downStations);
    }

    private Stations findStationsByDirection(final Section section, final Direction direction, final Long lineId) {
        final Set<Station> visitedStations = new HashSet<>();
        visitedStations.add(getStationByDirection(section, direction.getOpposite()));
        final Stations stations = new Stations();
        dfsStation(getStationByDirection(section, direction), visitedStations, stations, lineId);
        return stations;
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

    private void dfsStation(
            final Station station,
            final Set<Station> visitedStations,
            final Stations stations,
            final Long lineId
    ) {
        if (visitedStations.contains(station)) {
            return;
        }
        stations.add(station);
        visitedStations.add(station);

        final List<Section> sections = sectionsByStation.get(station).getSections();
        for (final Section section : sections) {
            final Station destination = getDestination(station, section);
            if (lineId.equals(section.getLineId())) {
                dfsStation(destination, visitedStations, stations, lineId);
            }
        }
    }

    private Station getDestination(final Station currentStation, final Section section) {
        if (section.getUpStation().equals(currentStation)) {
            return section.getDownStation();
        }
        return section.getUpStation();
    }

    public Sections findSections(final Section section) {
        final Long lineId = section.getLineId();
        final Sections upSections = findSectionsByDirection(section, Direction.UP, lineId);
        final Sections downSections = findSectionsByDirection(section, Direction.DOWN, lineId);
        return Sections.merge(upSections, section, downSections);
    }

    private Sections findSectionsByDirection(final Section section, final Direction direction, final Long lineId) {
        final Set<Station> visitedStations = new HashSet<>();
        visitedStations.add(getStationByDirection(section, direction.getOpposite()));
        final Sections sections = new Sections();
        dfsSection(getStationByDirection(section, direction), visitedStations, sections, lineId);
        return sections;
    }

    private void dfsSection(
            final Station station,
            final Set<Station> visitedStations,
            final Sections result,
            final Long lineId
    ) {
        visitedStations.add(station);
        final List<Section> sections = sectionsByStation.get(station).getSections();
        for (final Section section : sections) {
            final Station destination = getDestination(station, section);
            if (lineId.equals(section.getLineId()) && isNotVisitedStation(visitedStations, destination)) {
                result.add(section);
                dfsSection(destination, visitedStations, result, lineId);
            }
        }
    }

    private boolean isNotVisitedStation(final Set<Station> visitedStations, final Station station) {
        return !visitedStations.contains(station);
    }
}

package subway.domain;

import subway.domain.line.Line;
import subway.domain.line.Lines;
import subway.domain.line.edge.StationEdge;
import subway.domain.navigation.PathNavigation;
import subway.domain.path.SubwayPath;
import subway.domain.station.Station;
import subway.domain.station.Stations;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Subway {

    private final Lines lines;
    private final Stations stations;

    private Subway(final Lines lines, final Stations stations) {
        this.lines = lines;
        this.stations = stations;
    }

    public static Subway of(final Collection<Line> lines, final Collection<Station> stations) {
        final Lines initialLines = new Lines();
        initialLines.add(lines);
        final Stations initialStations = new Stations();
        initialStations.add(stations);
        return new Subway(initialLines, initialStations);
    }

    public void addLine(final Line line) {
        lines.add(line);
    }

    public void addStation(final Station station) {
        stations.add(station);
    }

    public void insertStationToLine(
            final Long lineId,
            final Long insertedStationId,
            final Long adjacentStationId,
            final LineDirection adjacentToInsertedDirection,
            final int distance
    ) {
        final Line line = lines.get(lineId);
        line.insertStation(insertedStationId, adjacentStationId, adjacentToInsertedDirection, distance);
    }

    public void removeStationFromLine(final Long lineId, final Long stationId) {
        final Line line = lines.get(lineId);
        line.removeStation(stationId);
    }

    public SubwayPath findPath(
            final Long startStationId,
            final Long destinationStationId,
            final PathNavigation pathNavigation
    ) {
        final List<StationEdge> allStationEdges = lines.getAllStationEdges();
        final List<Long> pathStations = pathNavigation.findPath(startStationId, destinationStationId, allStationEdges);
        return convertStationIdsToPath(pathStations);
    }

    private SubwayPath convertStationIdsToPath(final List<Long> stationIds) {
        final SubwayPath path = new SubwayPath();
        for (int index = 0; index < stationIds.size() - 1; index++) {
            final Long stationId = stationIds.get(index);
            final Long nextStationId = stationIds.get(index + 1);
            final Line line = getLine(lines.getLineIdBySection(stationId, nextStationId));
            final int distance = line.getEdgeDistanceBetween(stationId, nextStationId);

            path.add(line.getId(), new StationEdge(stationId, nextStationId, distance));
        }
        return path;
    }

    public List<Station> getStationsIn(final Long lineId) {
        final Line line = lines.get(lineId);
        return line.getStationIdsByOrder().stream()
                .map(stations::get)
                .collect(Collectors.toUnmodifiableList());
    }

    public Line getLine(final Long lineId) {
        return lines.get(lineId);
    }

    public List<Line> getLines() {
        return lines.toList();
    }

    public Station getStation(final Long stationId) {
        return stations.get(stationId);
    }

    public List<Station> getStations(final List<Long> stationIds) {
        return stationIds.stream()
                .map(stations::get)
                .collect(Collectors.toUnmodifiableList());
    }
}

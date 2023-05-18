package subway.domain;

import subway.domain.line.Line;
import subway.domain.line.Lines;
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

    public static Subway empty() {
        return new Subway(new Lines(), new Stations());
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

    public void addLine(final Collection<Line> lines) {
        this.lines.add(lines);
    }

    public void addStation(final Station station) {
        stations.add(station);
    }

    public void addStation(final Collection<Station> stations) {
        this.stations.add(stations);
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

    public List<Station> getStationsIn(final Long lineId) {
        final Line line = lines.get(lineId);
        return line.getStationIdsByOrder().stream()
                .map(stations::get)
                .collect(Collectors.toList());
    }

    public Line getLine(final Long lineId) {
        return lines.get(lineId);
    }

    public Set<Line> getLines() {
        return lines.toSet();
    }

    public Station getStation(final Long stationId) {
        return stations.get(stationId);
    }
}

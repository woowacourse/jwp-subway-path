package subway.domain;

import subway.exception.InvalidInputException;

import java.util.*;

public class Subway {
    private final List<Line> lines;

    private final JGraphPath JGraphPath;

    private Subway(List<Line> lines, Set<Station> stations, List<Section> sections) {
        this.lines = lines;
        this.JGraphPath = new JGraphPath(stations, sections);
    }

    public static Subway from(List<Line> lines){
        Set<Station> stations = new HashSet<>();
        lines.forEach(line -> stations.addAll(line.getAligned()));

        List<Section> sections = new ArrayList<>();

        for (Line line : lines) {
            sections.addAll(line.getSections());
        }
        return new Subway(lines, stations, sections);
    }

    public List<Station> findShortestPath(long startStationId, long endStationId) {
        return JGraphPath.findPath(getStation(startStationId), getStation(endStationId));
    }

    public int findShortestDistance(long startStationId, long endStationId) {
        return JGraphPath.findShortestDistance(getStation(startStationId), getStation(endStationId));
    }

    public int findFare(long startStationId, long endStationId) {
        int distance = findShortestDistance(startStationId, endStationId);

        if (distance <= 10) {
            return 1250;
        }

        if (distance <= 50) {
            return 1250 + calculateOver10kmFare(distance - 10);
        }

        return 1250 + calculateOver10kmFare(40) + calculateOver50kmFare(distance - 50);
    }

    private int calculateOver10kmFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int calculateOver50kmFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

    private Station getStation(long stationId) {
        return lines.stream().map(line -> line.findStationById(stationId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new InvalidInputException(stationId + "는 존재하지 않는 역 아이디입니다."));
    }
}

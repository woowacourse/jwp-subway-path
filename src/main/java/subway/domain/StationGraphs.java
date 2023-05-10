package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class StationGraphs {

    private static final StationGraphs INSTANCE = new StationGraphs();
    private final List<StationGraph> stationGraphs;

    private StationGraphs() {
        stationGraphs = new ArrayList<>();
    }

    public static StationGraphs getInstance() {
        return INSTANCE;
    }

    public void createLine(final Line line,
                           final Station upsLineStation,
                           final Station downLineStation,
                           final int distance) {
        final StationGraph stationGraph = new StationGraph(line);
        stationGraph.addStation(upsLineStation, downLineStation, distance);
        stationGraphs.add(stationGraph);
    }

    public StationGraph findStationGraphOf(Line line) {
        return stationGraphs.stream()
                .filter(stationGraph -> stationGraph.isSameLine(line))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("해당 호선의 정보가 존재하지 않습니다."));
    }

    public void deleteStationGraphOf(final Line line) {
        final StationGraph stationGraphOfLine = stationGraphs.stream()
                .filter(stationGraph -> stationGraph.isSameLine(line))
                .findAny().orElseThrow(() -> new NoSuchElementException("해당 호선이 존재하지 않습니다."));
        stationGraphs.remove(stationGraphOfLine);
    }

    public List<StationGraph> getStationGraphs() {
        return stationGraphs;
    }

    public void createStation(final Line line,
                              final Station station,
                              final Station adjacentStation,
                              final boolean isUpLine,
                              final double distance) {
        final StationGraph stationGraph = findStationGraphOf(line);

    }
}

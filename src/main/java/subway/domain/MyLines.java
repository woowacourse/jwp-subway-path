package subway.domain;

import java.util.List;

public class MyLines {

    private final List<MyLine> lines;

    public MyLines(List<MyLine> lines) {
        this.lines = lines;
    }

    public void addNewLine(String lineName, MyStation station1, MyStation station2, int distance) {
        // TODO : lineName이 이미 존재하는지 검증 (중복 검증)
        MyLine line = MyLine.createLine(lineName, station1, station2, distance);
        lines.add(line);
    }

    public void addStationToLine(String lineName, MyStation station1, MyStation station2, int distance) {
        MyLine myLine = lines.stream()
                .filter(line -> line.getName().equals(lineName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 호선이 없습니다."));
        myLine.addEdge(station1, station2, distance);
    }

    public List<MyStation> findAllStation(String lineName) {
        MyLine line = lines.stream()
                .filter(it -> it.getName().equals(lineName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 호선이 없습니다."));
        return line.getStations();

    }
}

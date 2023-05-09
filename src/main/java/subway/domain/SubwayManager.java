package subway.domain;

import java.util.List;
import java.util.Optional;

public class SubwayManager {

    private final MyLines lines;

    public SubwayManager(MyLines lines) {
        this.lines = lines;
    }

    public void createNewLine(String lineName, String stationName1, String stationName2, int distance) {
        MyStation station1 = new MyStation(stationName1);
        MyStation station2 = new MyStation(stationName2);
        lines.addNewLine(lineName, station1, station2, distance);
    }

    public void addStationToExistLine(String lineName, String stationName1, String stationName2, int distance) {
        MyStation station1 = new MyStation(stationName1);
        MyStation station2 = new MyStation(stationName2);
        lines.addStationToLine(lineName, station1, station2, distance);
    }

    public List<MyStation> findAllStation(String lineName) {
        return lines.findAllStation(lineName);
    }
}

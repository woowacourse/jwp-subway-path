package subway;

import subway.domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fixture {
    public static final Line line = new Line(1L, "2호선", "green");
    public static final Station stationA = new Station(1L, "A");
    public static final Station stationB = new Station(2L, "B");
    public static final Station stationC = new Station(3L, "C");
    public static final Section sectionAB = new Section(2, stationA, stationB, line);
    public static final Section sectionBA = new Section(2, stationB, stationA, line);
    public static final Section sectionBC = new Section(1, stationB, stationC, line);
    public static final Section sectionCB = new Section(1, stationC, stationB, line);
    public static final Map<Station, Sections> tempSubwayMap;

    static {
        tempSubwayMap = new HashMap<>();
        tempSubwayMap.put(stationA, new Sections(List.of(sectionAB)));
        tempSubwayMap.put(stationB, new Sections(List.of(sectionBA, sectionBC)));
        tempSubwayMap.put(stationC, new Sections(List.of(sectionCB)));
    }


    public static final SubwayMap subwayMap = new SubwayMap(tempSubwayMap);

}

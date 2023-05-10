package subway;

import subway.domain.Direction;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.SubwayMap;

import java.util.List;
import java.util.Map;

public class Fixture {

    public static final Station stationA = new Station(1L, "A");
    public static final Station stationB = new Station(2L, "B");
    public static final Station stationC = new Station(3L, "C");
    public static final Section sectionAB = new Section(2, 1L, 2L, Direction.DOWN);
    public static final Section sectionBA = new Section(2, 2L, 1L, Direction.UP);
    public static final Section sectionBC = new Section(1, 2L, 3L, Direction.DOWN);
    public static final Section sectionCB = new Section(1, 3L, 2L, Direction.UP);
    public static final Map<Station, List<Section>> tempSubwayMap = Map.of(
            stationA, List.of(sectionAB),
            stationB, List.of(sectionBA, sectionBC),
            stationC, List.of(sectionCB)
    );

    public static final SubwayMap subwayMap = new SubwayMap(tempSubwayMap);

}

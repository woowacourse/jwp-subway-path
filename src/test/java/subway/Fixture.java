package subway;

import subway.domain.Section;
import subway.domain.Station;

public class Fixture {
    public static final Station stationA = new Station(1L, "A");
    public static final Station stationB = new Station(2L, "B");
    public static final Station stationC = new Station(3L, "C");
    public static final Station stationD = new Station(4L, "D");
    public static final Section sectionAB = new Section(stationA, stationB, 10);
    public static final Section sectionBC = new Section(stationB, stationC, 10);
    public static final Section sectionCD = new Section(stationC, stationD, 10);
    public static final Section sectionAC = new Section(stationA, stationC, 5);
    public static final Section sectionCB = new Section(stationC, stationB, 5);

}

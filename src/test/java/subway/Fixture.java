package subway;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.SectionMap;
import subway.domain.Station;

import java.util.List;

public class Fixture {
    public static final Line line = new Line(1L, "2호선", "green");
    public static final Station stationA = new Station(1L, "A");
    public static final Station stationB = new Station(2L, "B");
    public static final Station stationC = new Station(3L, "C");
    public static final Station stationD = new Station(4L, "D");
    public static final Station stationE = new Station(5L, "E");
    public static final Section sectionAB = new Section(stationA, stationB, 10);
    public static final Section sectionBC = new Section(stationB, stationC, 10);
    public static final SectionMap sectionMapABC = SectionMap.generateBySections(List.of(sectionAB, sectionBC), stationA);
    public static final Line lineABC = new Line(2L, "ABC", "yellow", sectionMapABC);
    public static final Section sectionBD = new Section(stationB, stationD, 10);
    public static final Section sectionCD = new Section(stationC, stationD, 10);
    public static final Section sectionAC = new Section(stationA, stationC, 5);
    public static final Section sectionCB = new Section(stationC, stationB, 5);
    public static final Section sectionDE = new Section(stationD, stationE, 10);
    public static final SectionMap sectionMapBDE = SectionMap.generateBySections(List.of(sectionBD, sectionDE), stationB);
    public static final Line lineBDE = new Line(3L, "BDE", "pink", sectionMapBDE);

}

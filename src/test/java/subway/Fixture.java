package subway;

import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;

public class Fixture {
    public static final Station stationA = new Station(1L, "A");
    public static final Station stationB = new Station(2L, "B");
    public static final Station stationC = new Station(3L, "C");
    public static final Station stationD = new Station(4L, "D");
    public static final Station stationE = new Station(5L, "E");
    public static final Station stationF = new Station(6L, "F");
    public static final Distance distance1 = new Distance(1);
    public static final Distance distance2 = new Distance(2);
    public static final Section sectionAB = new Section(stationA, stationB, distance2);
    public static final Section sectionBC = new Section(stationB, stationC, distance1);
    public static final Section sectionCD = new Section(stationC, stationD, distance1);
    public static final Section sectionDE = new Section(stationD, stationE, distance1);
    public static final Section sectionAF = new Section(stationA, stationF, new Distance(2));
    public static final Section sectionFE = new Section(stationF, stationE, new Distance(3));

    public static final Line line1 = new Line(1L, "1호선", "blue", new Sections(List.of(sectionAB, sectionBC)));
    public static final Line line2 = new Line(2L, "2호선", "green", new Sections());
    public static final Line line3 = new Line(3L, "3호선", "orange", new Sections());
}

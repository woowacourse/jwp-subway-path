package subway;

import subway.domain.*;

import java.util.List;

public class Fixture {
    public static final Station stationA = new Station(1L, "A");
    public static final Station stationB = new Station( 2L, "B");
    public static final Station stationC = new Station(3L, "C");
    public static final Distance distance1 = new Distance(1);
    public static final Distance distance2 = new Distance(2);
    public static final Section sectionAB = new Section(stationA, stationB, distance2);
    public static final Section sectionBC = new Section(stationB, stationC, distance1);
    private static final Sections sections = new Sections(List.of(sectionAB, sectionBC));
    public static final Line line1 = new Line( 1L, "1호선", "blue", new Sections(List.of(sectionAB, sectionBC)));
    public static final Line line2 = new Line(2L, "2호선", "green", new Sections());
    public static final Line line3 = new Line(3L, "3호선", "orange", new Sections());
}

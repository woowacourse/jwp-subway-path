package subway.domain;

import java.util.List;

public class SectionFixture {

    public static final Line LINE1 = new Line(1L, "1호선", "파랑색");
    public static final Line LINE2 = new Line(2L, "2호선", "초록색");

    public static final Station STATION1 = new Station(1L, "1L");
    public static final Station STATION2 = new Station(2L, "2L");
    public static final Station STATION3 = new Station(3L, "3L");
    public static final Station STATION4 = new Station(4L, "4L");
    public static final Station STATION5 = new Station(5L, "5L");
    public static final Station STATION6 = new Station(6L, "6L");
    public static final Station STATION7 = new Station(7L, "7L");
    public static final Station STATION8 = new Station(8L, "8L");
    public static final Station STATION9 = new Station(9L, "9L");
    public static final Station STATION10 = new Station(10L, "10L");
    public static final Station STATION11 = new Station(11L, "11L");
    public static final Station STATION12 = new Station(12L, "12L");

    public static final List<Section> SECTIONS1 = List.of(
            new Section(1L, 1L, STATION1, STATION2, new Distance(3)),
            new Section(2L, 1L, STATION2, STATION3, new Distance(3)),
            new Section(3L, 1L, STATION3, STATION4, new Distance(3)),
            new Section(4L, 1L, STATION4, STATION5, new Distance(3)),
            new Section(5L, 1L, STATION5, STATION6, new Distance(3)),
            new Section(6L, 1L, STATION6, STATION7, new Distance(3))
    );

    public static final List<Section> SECTIONS2 = List.of(
            new Section(4L, 1L, STATION4, STATION5, new Distance(3)),
            new Section(6L, 1L, STATION6, STATION7, new Distance(3)),
            new Section(2L, 1L, STATION2, STATION3, new Distance(3)),
            new Section(5L, 1L, STATION5, STATION6, new Distance(3)),
            new Section(1L, 1L, STATION1, STATION2, new Distance(3)),
            new Section(3L, 1L, STATION3, STATION4, new Distance(3))
    );

    public static final List<Section> SECTIONS3 = List.of(
            new Section(1L, 1L, STATION4, STATION5, new Distance(3)),
            new Section(2L, 1L, STATION6, STATION7, new Distance(3)),
            new Section(3L, 1L, STATION2, STATION3, new Distance(3)),
            new Section(4L, 1L, STATION5, STATION6, new Distance(3)),
            new Section(5L, 1L, STATION1, STATION2, new Distance(3)),
            new Section(6L, 1L, STATION3, STATION4, new Distance(3))
    );

    public static final List<Section> SECTIONS4 = List.of(
            new Section(1L, 1L, STATION1, STATION2, new Distance(3)),
            new Section(2L, 1L, STATION2, STATION3, new Distance(3)),
            new Section(3L, 1L, STATION3, STATION4, new Distance(3)),
            new Section(4L, 1L, STATION4, STATION5, new Distance(3)),
            new Section(5L, 1L, STATION5, STATION6, new Distance(3)),
            new Section(6L, 1L, STATION6, STATION7, new Distance(3)),

            new Section(7L, 2L, STATION8, STATION9, new Distance(3)),
            new Section(8L, 2L, STATION9, STATION10, new Distance(3)),
            new Section(9L, 2L, STATION10, STATION4, new Distance(3)),
            new Section(10L, 2L, STATION4, STATION11, new Distance(3)),
            new Section(11L, 2L, STATION11, STATION12, new Distance(3))
    );
}

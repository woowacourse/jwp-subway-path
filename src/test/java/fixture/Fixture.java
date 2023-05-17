package fixture;

import java.util.List;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

public class Fixture {
    public static final Line LINE_1 = new Line(1L, "1호선", "파랑");
    public static final Line LINE_2 = new Line(2L, "2호선", "초록");
    public static final Line LINE_3 = new Line(3L, "3호선", "주황");
    public static final List<Line> LINES = List.of(LINE_1, LINE_2, LINE_3);
    public static final Station STATION_1 = new Station(1L, "잠실");
    public static final Station STATION_2 = new Station(2L, "영등포");
    public static final Station STATION_3 = new Station(3L, "정자");
    public static final Station STATION_4 = new Station(4L, "미금");
    public static final Station STATION_5 = new Station(5L, "신도림");
    public static final Station STATION_6 = new Station(6L, "수내");
    public static final Station STATION_7 = new Station(7L, "서현");
    public static final Station STATION_8 = new Station(8L, "이매");
    public static final Section SECTION_1 = new Section(1L, 2L, 1L, 2);
    public static final Section SECTION_2 = new Section(2L, 3L, 1L, 3);
    public static final Section SECTION_3 = new Section(3L, 4L, 1L, 4);
    public static final Section SECTION_4 = new Section(4L, 5L, 1L, 5);
    public static final Section SECTION_5 = new Section(3L, 6L, 2L, 6);
    public static final Section SECTION_6 = new Section(6L, 7L, 2L, 7);
    public static final Section SECTION_7 = new Section(7L, 8L, 2L, 8);
    public static final Section SECTION_8 = new Section(1L, 4L, 3L, 4);
    public static final Section SECTION_9 = new Section(4L, 6L, 3L, 5);
    public static final Section SECTION_10 = new Section(6L, 8L, 3L, 6);
    public static final List<Section> SECTIONS = List.of(
            SECTION_1,
            SECTION_2,
            SECTION_3,
            SECTION_4,
            SECTION_5,
            SECTION_6,
            SECTION_7,
            SECTION_8,
            SECTION_9,
            SECTION_10
    );

    public static final List<Station> STATIONS = List.of(
            STATION_1,
            STATION_2,
            STATION_3,
            STATION_4,
            STATION_5,
            STATION_6,
            STATION_7,
            STATION_8
    );
}

package subway.domain.section;

import java.util.List;

import subway.domain.line.Line;
import subway.domain.station.Station;

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
            Section.builder().lineId(1L).distance(3)
                    .id(1L).upStation(STATION1).downStation(STATION2).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(2L).upStation(STATION2).downStation(STATION3).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(3L).upStation(STATION3).downStation(STATION4).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(4L).upStation(STATION4).downStation(STATION5).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(5L).upStation(STATION5).downStation(STATION6).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(6L).upStation(STATION6).downStation(STATION7).build()
    );

    public static final List<Section> SECTIONS2 = List.of(
            Section.builder().lineId(1L).distance(3)
                    .id(4L).upStation(STATION4).downStation(STATION5).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(6L).upStation(STATION6).downStation(STATION7).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(2L).upStation(STATION2).downStation(STATION3).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(5L).upStation(STATION5).downStation(STATION6).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(1L).upStation(STATION1).downStation(STATION2).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(3L).upStation(STATION3).downStation(STATION4).build()
    );

    public static final List<Section> SECTIONS3 = List.of(
            Section.builder().lineId(1L).distance(3)
                    .id(1L).upStation(STATION4).downStation(STATION5).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(2L).upStation(STATION6).downStation(STATION7).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(3L).upStation(STATION2).downStation(STATION3).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(4L).upStation(STATION5).downStation(STATION6).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(5L).upStation(STATION1).downStation(STATION2).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(6L).upStation(STATION3).downStation(STATION4).build()
    );

    public static final List<Section> SECTIONS4 = List.of(
            Section.builder().lineId(1L).distance(3)
                    .id(1L).upStation(STATION1).downStation(STATION2).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(2L).upStation(STATION2).downStation(STATION3).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(3L).upStation(STATION3).downStation(STATION4).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(4L).upStation(STATION4).downStation(STATION5).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(5L).upStation(STATION5).downStation(STATION6).build(),
            Section.builder().lineId(1L).distance(3)
                    .id(6L).upStation(STATION6).downStation(STATION7).build(),


            Section.builder().lineId(2L).distance(3)
                    .id(7L).upStation(STATION8).downStation(STATION9).build(),
            Section.builder().lineId(2L).distance(3)
                    .id(8L).upStation(STATION9).downStation(STATION10).build(),
            Section.builder().lineId(2L).distance(3)
                    .id(9L).upStation(STATION10).downStation(STATION4).build(),
            Section.builder().lineId(2L).distance(3)
                    .id(10L).upStation(STATION4).downStation(STATION11).build(),
            Section.builder().lineId(2L).distance(3)
                    .id(11L).upStation(STATION11).downStation(STATION12).build()
    );

    public static final List<Section> SECTIONS5 = List.of(
            Section.builder().lineId(1L).distance(3)
                    .id(1L).upStation(STATION1).downStation(STATION2).build(),
            Section.builder().lineId(1L).distance(2)
                    .id(2L).upStation(STATION2).downStation(STATION3).build(),
            Section.builder().lineId(1L).distance(10)
                    .id(3L).upStation(STATION3).downStation(STATION4).build(),


            Section.builder().lineId(2L).distance(3)
                    .id(4L).upStation(STATION3).downStation(STATION5).build(),
            Section.builder().lineId(2L).distance(5)
                    .id(5L).upStation(STATION5).downStation(STATION4).build(),
            Section.builder().lineId(2L).distance(6)
                    .id(6L).upStation(STATION4).downStation(STATION6).build(),


            Section.builder().lineId(3L).distance(4)
                    .id(7L).upStation(STATION8).downStation(STATION4).build(),
            Section.builder().lineId(3L).distance(7)
                    .id(8L).upStation(STATION4).downStation(STATION9).build()
    );
}

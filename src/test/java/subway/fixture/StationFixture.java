package subway.fixture;

import java.util.List;
import subway.dao.entity.SectionEntity;
import subway.domain.line.Line;
import subway.domain.station.Station;

public final class StationFixture {
    public static final Line 이호선 = new Line(1L, "이호선", "bg-green-600");
    public static final Line 팔호선 = new Line(2L, "팔호선", "bg-pink-600");

    public static final Station 잠실역 = new Station(1L,"잠실역");
    public static final Station 선릉역 = new Station(2L, "선릉역");
    public static final Station 강남역 = new Station(3L, "강남역");
    public static final Station 신림역 = new Station(4L, "신림역");
    public static final Station 복정역 = new Station(4L, "복정역");
    public static final Station 남위례역 = new Station(5L, "남위례역");
    public static final Station 산성역 = new Station(6L, "산성역");

    public static final SectionEntity 잠실_선릉 = new SectionEntity(1L,1L, 1L, 2L, 10);
    public static final SectionEntity 선릉_강남 = new SectionEntity(2L, 1L, 2L, 3L, 10);

    public static final SectionEntity 복정_남위례 = new SectionEntity(3L, 2L, 4L, 5L, 10);
    public static final SectionEntity 남위례_산성 = new SectionEntity(4L, 2L, 5L, 6L, 10);

    public static final List<SectionEntity> 이호선_역들 = List.of(잠실_선릉, 선릉_강남);
    public static final List<SectionEntity> 팔호선_역들 = List.of(복정_남위례, 남위례_산성);
}

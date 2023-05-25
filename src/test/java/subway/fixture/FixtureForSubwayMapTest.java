package subway.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import subway.business.domain.line.Line;
import subway.business.domain.line.Section;
import subway.business.domain.line.Station;
import subway.business.domain.transfer.Transfer;

public class FixtureForSubwayMapTest {

    // 2호선 역
    public static Station 봉천 = new Station(1L, "봉천");
    public static Station 서울대입구 = new Station(2L, "서울대입구");
    public static Station 낙성대 = new Station(3L, "낙성대");
    public static Station 사당_2호선 = new Station(4L, "사당");
    public static Station 방배 = new Station(5L, "방배");
    public static Station 서초 = new Station(6L, "서초");
    public static Station 교대_2호선 = new Station(7L, "교대");
    public static Station 강남 = new Station(8L, "강남");

    // 3호선 역
    public static Station 남부터미널 = new Station(9L, "남부터미널");
    public static Station 교대_3호선 = new Station(10L, "교대");
    public static Station 고속터미널 = new Station(11L, "고속터미널");

    // 4호선 역
    public static Station 남태령 = new Station(12L, "남태령");
    public static Station 사당_4호선 = new Station(13L, "사당");
    public static Station 총신대입구 = new Station(14L, "총신대입구");
    public static Station 동작 = new Station(15L, "동작");

    // 2호선 구간
    public static Section 봉천_서울대입구 = new Section(1L, 봉천, 서울대입구, 5);
    public static Section 서울대입구_낙성대 = new Section(2L, 서울대입구, 낙성대, 5);
    public static Section 낙성대_사당 = new Section(3L, 낙성대, 사당_2호선, 5);
    public static Section 사당_방배 = new Section(4L, 사당_2호선, 방배, 5);
    public static Section 방배_서초 = new Section(5L, 방배, 서초, 5);
    public static Section 서초_교대 = new Section(6L, 서초, 교대_2호선, 5);
    public static Section 교대_강남 = new Section(7L, 교대_2호선, 강남, 5);

    // 3호선 구간
    public static Section 남부터미널_교대 = new Section(8L, 남부터미널, 교대_3호선, 5);
    public static Section 교대_고속터미널 = new Section(9L, 교대_3호선, 고속터미널, 5);

    // 4호선 구간
    public static Section 남태령_사당 = new Section(10L, 남태령, 사당_4호선, 5);
    public static Section 사당_총신대입구 = new Section(11L, 사당_4호선, 총신대입구, 5);
    public static Section 총신대입구_동작 = new Section(12L, 총신대입구, 동작, 5);

    // 4호선 노선
    public static Line 이호선 = new Line(
            1L,
            "2호선",
            new ArrayList<>(List.of(
                    봉천_서울대입구, 서울대입구_낙성대, 낙성대_사당, 사당_방배, 방배_서초, 서초_교대, 교대_강남
            )),
            BigDecimal.valueOf(0)
    );

    public static Line 삼호선 = new Line(
            2L,
            "3호선",
            new ArrayList<>(List.of(
                    남부터미널_교대, 교대_고속터미널
            )),
            BigDecimal.valueOf(100)
    );

    public static Line 사호선 = new Line(
            3L,
            "4호선",
            new ArrayList<>(List.of(
                    남태령_사당, 사당_총신대입구, 총신대입구_동작
            )),
            BigDecimal.valueOf(200)
    );

    // 환승
    public static Transfer 사당_2호선_4호선 = new Transfer(1L, 사당_2호선, 사당_4호선);
    public static Transfer 교대_2호선_3호선 = new Transfer(2L, 교대_2호선, 교대_3호선);
}

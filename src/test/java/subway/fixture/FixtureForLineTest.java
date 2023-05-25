package subway.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import subway.business.domain.line.Line;
import subway.business.domain.line.Section;
import subway.business.domain.line.Station;
import subway.business.service.dto.LineSaveRequest;
import subway.business.service.dto.StationAddToLineRequest;

public class FixtureForLineTest {


    public static LineSaveRequest LINE_SAVE_REQUEST = new LineSaveRequest(
            "2호선",
            0, "몽촌토성역", "잠실역",
            5
    );

    public static StationAddToLineRequest STATION_ADD_TO_LINE_REQUEST = new StationAddToLineRequest(
            "까치산역",
            "몽촌토성역",
            "하행",
            5
    );

    /**
     * @ID : 1L
     * @이름 : 2호선
     * @구간상태 : (상행) 잠실역 --(5)-- 몽촌토성역 (하행)
     */
    public static Line line2WithOneSection() {
        return new Line(
                1L,
                "2호선",
                new ArrayList<>(List.of(
                        new Section(1L, new Station(1L, "잠실역"), new Station(2L, "몽촌토성역"), 5)
                )),
                BigDecimal.valueOf(0)
        );
    }

    /**
     * @ID : 1L
     * @이름 : 2호선
     * @구간상태 : (상행) 잠실역 --(5)-- 몽촌토성역 --(5)-- 까치산역 (하행)
     */
    public static Line line2WithTwoSection() {
        return new Line(
                1L,
                "2호선",
                new ArrayList<>(List.of(
                        new Section(1L, new Station(1L, "잠실역"), new Station(2L, "몽촌토성역"), 5),
                        new Section(2L, new Station(3L, "몽촌토성역"), new Station(4L, "까치산역"), 5))
                ),
                BigDecimal.valueOf(0)
        );
    }

    public static Line line2FromBongcheonToGangnam() {
        return new Line(
                1L,
                "2호선",
                new ArrayList<>(List.of(
                        new Section(1L, new Station(1L, "봉천역"), new Station(2L, "서울대입구역"), 5),
                        new Section(2L, new Station(2L, "서울대입구역"), new Station(3L, "낙성대역"), 5),
                        new Section(3L, new Station(3L, "낙성대역"), new Station(4L, "사당역"), 5),
                        new Section(4L, new Station(4L, "사당역"), new Station(5L, "방배역"), 5),
                        new Section(5L, new Station(5L, "방배역"), new Station(6L, "서초역"), 5),
                        new Section(6L, new Station(6L, "서초역"), new Station(7L, "교대역"), 5),
                        new Section(7L, new Station(7L, "교대역"), new Station(8L, "강남역"), 5))
                ),
                BigDecimal.valueOf(0)
        );
    }

    public static Line line3FromNambuBusTerminalToExpressBusTerminal() {
        return new Line(
                2L,
                "3호선",
                new ArrayList<>(List.of(
                        new Section(8L, new Station(9L, "남부터미널역"), new Station(10L, "교대역"), 5),
                        new Section(9L, new Station(10L, "교대역"), new Station(11L, "고속터미널역"), 5))
                ),
                BigDecimal.valueOf(100)
        );
    }

    public static Line line4FromNamtaeryeongToDongjak() {
        return new Line(
                3L,
                "4호선",
                new ArrayList<>(List.of(
                        new Section(10L, new Station(12L, "남태령역"), new Station(13L, "사당역"), 5),
                        new Section(11L, new Station(13L, "사당역"), new Station(14L, "총신대입구역"), 5),
                        new Section(12L, new Station(14L, "총신대입구역"), new Station(15L, "동작역"), 5))
                ),
                BigDecimal.valueOf(0)
        );
    }

    public static Station station1() {
        return new Station(1L, "잠실역");
    }

    public static Station station2() {
        return new Station(2L, "몽촌토성역");
    }

    public static Station station3() {
        return new Station(3L, "까치산역");
    }
}

package subway.fixture;

import java.util.ArrayList;
import java.util.List;
import subway.business.domain.Line;
import subway.business.domain.Section;
import subway.business.domain.Station;
import subway.business.service.dto.LineSaveRequest;
import subway.business.service.dto.StationAddToLineRequest;

public class Fixture {

    public static LineSaveRequest LINE_SAVE_REQUEST = new LineSaveRequest(
            "2호선",
            "잠실역",
            "몽촌토성역",
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
                        new Section(1L, new Station(1L, "잠실역"), new Station(2L, "몽촌토성역"), 5))
                )
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
                )
        );
    }
}

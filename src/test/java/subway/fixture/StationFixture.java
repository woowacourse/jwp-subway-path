package subway.fixture;

import subway.domain.*;
import subway.dto.StationResponse;


public class StationFixture {

    public static final Station 잠실새내 = new Station(1L, "잠실새내");
    public static final Station 잠실 = new Station(2L, "잠실");
    public static final Station 잠실나루 = new Station(3L, "잠실나루");
    public static final Station 몽촌토성 = new Station(4L, "몽촌토성");
    public static final Station 석촌 = new Station(5L, "석촌");

    public static final StationResponse 잠실새내_응답 = new StationResponse(1L, "잠실새내");
    public static final StationResponse 잠실_응답 = new StationResponse(2L, "잠실");
    public static final StationResponse 잠실나루_응답 = new StationResponse(3L, "잠실나루");
    public static final StationResponse 몽촌토성_응답 = new StationResponse(4L, "몽촌토성");
    public static final StationResponse 석촌_응답 = new StationResponse(5L, "석촌");
}

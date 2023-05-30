package subway.fixtures.domain;

import subway.domain.Position;
import subway.domain.Station;

import static subway.domain.Position.*;

public class StationFixture {

    // 위치를 가지고 있지 않는 역
    public static final Station JAMSIL = Station.of(1L, "잠실역");
    public static final Station SEOLLEUNG = Station.of(2L, "선릉역");
    public static final Station SADANG = Station.of(3L, "사당역");
    public static final Station HONGDAE = Station.of(4L, "홍대입구역");
    public static final Station DMC = Station.of(5L, "디지털미디어시티역");

    // 위치를 가지고 있는 역
    public static final Station JAMSIL_UP = Station.of(1L, "잠실역", UP);
    public static final Station SEOLLEUNG_MID = Station.of(2L, "선릉역", MID);
    public static final Station SEOLLEUNG_DOWN = Station.of(2L, "선릉역", DOWN);
    public static final Station SADANG_MID = Station.of(3L, "사당역", MID);
    public static final Station SADANG_DOWN = Station.of(3L, "사당역", DOWN);
    public static final Station HONDDAE_DOWN = Station.of(4L, "홍대입구역", DOWN);
}

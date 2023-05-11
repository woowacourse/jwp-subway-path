package subway.domain;

import subway.service.station.domain.Station;

public class StationFixture {
    public static Station JAMSIL = new Station(1L, "잠실");
    public static Station SEONLEUNG = new Station(2L, "선릉");
    public static Station GANGNAM = new Station(3L, "강남");
    public static Station YUKSAM = new Station(4L, "역삼");

    public static Station JAMSIL_NO_ID = new Station(null, "잠실");
    public static Station SEONLEUNG_NO_ID = new Station(null, "선릉");
    public static Station GANGNAM_NO_ID = new Station(null, "강남");
    public static Station YUKSAM_NO_ID = new Station(null, "역삼");
}

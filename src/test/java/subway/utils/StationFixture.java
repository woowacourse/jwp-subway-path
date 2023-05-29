package subway.utils;

import subway.domain.Station;
import subway.domain.Stations;

import java.util.Set;

public class StationFixture {

    public static final Station SULLEUNG_STATION = new Station("선릉");
    public static final Station JAMSIL_STATION = new Station("잠실");
    public static final Station JAMSILNARU_STATION = new Station("잠실나루");
    public static final Stations SULLEUNG_JAMSIL_JAMSILNARU = new Stations(Set.of(SULLEUNG_STATION, JAMSIL_STATION, JAMSILNARU_STATION));
}

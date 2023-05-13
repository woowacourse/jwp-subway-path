package subway.domain.interstation;

import static subway.domain.station.StationFixture.누누_역_id_2;
import static subway.domain.station.StationFixture.두둠_역_id_3;
import static subway.domain.station.StationFixture.처음보는_역_id_4;
import static subway.domain.station.StationFixture.코다_역_id_1;

public class InterStationFixture {

    public static final InterStation 코다에서_누누_구간_id_1 = new InterStation(1L, 코다_역_id_1, 누누_역_id_2, 10);
    public static final InterStation 누누에서_두둠_구간_id_2 = new InterStation(2L, 누누_역_id_2, 두둠_역_id_3, 10);
    public static final InterStation 두둠에서_처음보는_역_id_3 = new InterStation(3L, 두둠_역_id_3, 처음보는_역_id_4, 3L);
}

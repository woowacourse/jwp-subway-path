package subway.interstation.domain;

import static subway.station.domain.StationFixture.누누_역_id_2;
import static subway.station.domain.StationFixture.두둠_역_id_3;
import static subway.station.domain.StationFixture.처음보는_역_id_4;
import static subway.station.domain.StationFixture.코다_역_id_1;

import subway.line.domain.interstation.InterStation;

public class InterStationFixture {

    public static final InterStation 코다에서_누누_구간_id_1 = new InterStation(1L, 코다_역_id_1.getId(), 누누_역_id_2.getId(), 10);
    public static final InterStation 누누에서_두둠_구간_id_2 = new InterStation(2L, 누누_역_id_2.getId(), 두둠_역_id_3.getId(), 10);
    public static final InterStation 두둠에서_처음보는_역_id_3 = new InterStation(3L, 두둠_역_id_3.getId(), 처음보는_역_id_4.getId(),
            3L);
}

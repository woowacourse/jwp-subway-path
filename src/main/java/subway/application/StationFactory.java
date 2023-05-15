package subway.application;

import subway.dao.StationEntity;
import subway.domain.Station;

public class StationFactory {

    public static Station toStation(final StationEntity stationEntity) {
        return new Station(stationEntity.getName());
    }

}

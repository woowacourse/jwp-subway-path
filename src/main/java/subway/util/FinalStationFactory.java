package subway.util;

import org.springframework.stereotype.Component;
import subway.dao.StationDao;
import subway.domain.FinalStation;

@Component
public class FinalStationFactory {

    private final StationDao stationDao;

    public FinalStationFactory(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public FinalStation getFinalStation(final Long lineId) {
        final String finalUpStationName = stationDao.findFinalUpStation(lineId).getName();
        final String finalDownStationName = stationDao.findFinalDownStation(lineId).getName();
        return FinalStation.of(finalUpStationName, finalDownStationName);
    }

}

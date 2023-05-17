package subway.global.util;

import org.springframework.stereotype.Component;
import subway.domain.FinalStation;
import subway.domain.dao.StationDao;
import subway.exception.StationNotFoundException;

@Component
public class FinalStationFactory {

    private final StationDao stationDao;

    public FinalStationFactory(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public FinalStation getFinalStation(final Long lineId) {
        final String finalUpStationName = stationDao.findFinalUpStation(lineId)
                .orElseThrow(() -> StationNotFoundException.THROW).getName();
        final String finalDownStationName = stationDao.findFinalDownStation(lineId)
                .orElseThrow(() -> StationNotFoundException.THROW).getName();
        return FinalStation.of(finalUpStationName, finalDownStationName);
    }

}

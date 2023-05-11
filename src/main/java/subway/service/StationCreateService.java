package subway.service;

import subway.dao.StationDao;

public class StationCreateService {

    private final StationDao stationDao;

    public StationCreateService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }
}

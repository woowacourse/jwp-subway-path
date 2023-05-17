package subway.service;

import subway.dao.StationDao;
import subway.domain.Stations;
import subway.dto.StationRequest;

public class StationService {

    private final StationDao stationDao;
    private final Stations stations;

    public StationService(StationDao stationDao, Stations stations) {
        this.stationDao = stationDao;
        this.stations = stations;
    }

    public void addStations(StationRequest request) {
//        //이거 첫 station 생성하고 id 받아오자 method 빼자
//        Station station;
//        if (!stations.findByName(request.getName()).isPresent()) {
//            station = new Station(null, request.getName(), new Distance(request.getDistance()), request.);
//        } else {
//            station = stations.findByName(request.getName()).get();
//        }
//        stations.addStation(station);
//
//        StationEntity stationEntity = new StationEntity(null, station.getName(), request.getDistance());
//        stationDao.insertStations()
    }
}

package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.StationResponse;
import subway.entity.LineEntity;
import subway.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationDao stationDao;
    private final LineDao lineDao;

    public StationService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public List<StationResponse> findLineStationResponsesById(Long lineId){
        LineEntity lineEntity=lineDao.findById(lineId);
        Long headStationId=lineEntity.getHeadStation();
        StationEntity headEntity = stationDao.findById(headStationId);
        List<StationEntity> entities = stationDao.findByLineId(lineId);

        Station headStation = Station.from(entities, headEntity);
        Line line = Line.from(lineEntity, headStation);
        return line.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

//    public StationResponse saveStation(StationRequest stationRequest) {
//        StationEntity station = stationDao.insert(new Station(stationRequest.getName()));
//        return StationResponse.of(station);
//    }

//    public StationResponse findStationResponseById(Long id) {
//        return StationResponse.of(stationDao.findById(id));
//    }

    public List<StationEntity> findAllStationTest() {
        return stationDao.findAll();
    }


//    public void updateStation(Long id, StationRequest stationRequest) {
//        stationDao.update(new Station(id, stationRequest.getName()));
//    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}

package subway.application;

import java.util.ArrayList;
import java.util.Objects;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dto.StationResponse;
import subway.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    public static final int MIN_STATION_COUNT = 2;
    public static final long EMPTY_STATION_ID = 0L;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public StationService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public List<StationResponse> findLineStationResponsesById(Long lineId) {
        List<StationEntity> rawStations = stationDao.findByLineId(lineId);
        StationEntity currentStation = stationDao.findHeadStationByLineId(lineId);
        return sortStations(rawStations, currentStation).stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    private static List<StationEntity> sortStations(List<StationEntity> rawStations,
        StationEntity currentStation) {
        List<StationEntity> sortedStations = new ArrayList<>(List.of(currentStation));
        while (currentStation.getNext() != EMPTY_STATION_ID) {
            StationEntity finalCurrentStation = currentStation;
            currentStation = rawStations.stream()
                .filter(entity -> Objects.equals(entity.getId(), finalCurrentStation.getNext()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("노선에 잘못된 역 정보가 포함되어 있습니다."));
            sortedStations.add(currentStation);
        }
        return sortedStations;
    }

//    private Line generateLine(Long lineId) {
//        LineEntity lineEntity = lineDao.findById(lineId);
//        Long headStationId = lineEntity.getHeadStation();
//        StationEntity headEntity = stationDao.findById(headStationId);
//        List<StationEntity> entities = stationDao.findByLineId(lineId);
//
//        Station headStation = Station.from(entities, headEntity);
//        Line line = Line.from(lineEntity, headStation);
//        return line;
//    }

//    public StationResponse saveStation(StationRequest stationRequest) {
//        StationEntity station = stationDao.insert(new Station(stationRequest.getName()));
//        return StationResponse.of(station);
//    }

//    public StationResponse findStationResponseById(Long id) {
//        return StationResponse.of(stationDao.findById(id));
//    }
//    public void updateStation(Long id, StationRequest stationRequest) {
//        stationDao.update(new Station(id, stationRequest.getName()));
//    }

    public void deleteStation(Long lineId, String name) {
        validateCanDelete(lineId, name);
        StationEntity deleteStation = stationDao.findByLineIdAndName(lineId, name);
        if (lineDao.isUpEndStation(lineId, name)) {
            deleteUpEndStation(lineId, deleteStation);
            return;
        }
        StationEntity upStation = stationDao.findByNextStationId(lineId,
            deleteStation.getId());
        if (stationDao.isDownEndStation(lineId, name)) {
            deleteDownEndStation(deleteStation, upStation);
            return;
        }
        deleteMiddleStation(deleteStation, upStation);
    }

    private void validateCanDelete(Long lineId, String name) {
        validateStationCount(lineId);
        validateExistStation(lineId, name);
    }

    private void validateExistStation(Long lineId, String name) {
        stationDao.findByLineIdAndName(lineId, name);
    }

    private void validateStationCount(Long lineId) {
        if (stationDao.findByLineId(lineId).size() <= MIN_STATION_COUNT) {
            throw new IllegalArgumentException("노선에는 최소 2개 이상의 역이 존재해야 합니다.");
        }
    }

    private void deleteUpEndStation(Long lineId, StationEntity deleteStation) {
        lineDao.updateHeadStation(lineId, deleteStation.getNext());
        stationDao.deleteById(deleteStation.getId());
    }

    private void deleteDownEndStation(StationEntity deleteStation, StationEntity upStation) {
        stationDao.updateNextStationById(upStation.getId(), EMPTY_STATION_ID);
        stationDao.deleteById(deleteStation.getId());
    }

    private void deleteMiddleStation(StationEntity deleteStation, StationEntity upStation) {
        int newDistance = upStation.getDistance() + deleteStation.getDistance();
        stationDao.updateDistanceById(upStation.getId(), newDistance);
        stationDao.updateNextStationById(upStation.getId(), deleteStation.getNext());
        stationDao.deleteById(deleteStation.getId());
    }
}

package subway.application;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class StationService {

    private static final int MIN_STATION_COUNT = 2;
    public static final long EMPTY_STATION_ID = 0L;
    public static final int MIN_DISTANCE_VALUE = 1;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public StationService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public Long saveStation(Long lineId, StationRequest request) {
        validateBothExist(lineId, request);
        validateBothNotExist(lineId, request);
        if (lineDao.isUpEndStation(lineId, request.getDownStation())) {   //상행 종점 추가
            return saveUpEndStation(lineId, request);
        }
        if (stationDao.isDownEndStation(lineId, request.getUpStation())) { //하행 종점 추가
            return saveDownEndStation(lineId, request);
        }
        return saveMiddle(lineId, request);
    }

    private void validateBothExist(Long lineId, StationRequest request) {
        if (isExistBoth(lineId, request)) {
            throw new DuplicateKeyException("이미 존재하는 역입니다.");
        }
    }

    private boolean isExistBoth(Long lineId, StationRequest request) {
        return stationDao.isExistInLine(lineId, request.getUpStation())
            && stationDao.isExistInLine(lineId, request.getDownStation());
    }

    private void validateBothNotExist(Long lineId, StationRequest request) {
        if (isNotExistBoth(lineId, request)) {
            throw new NoSuchElementException("해당 노선에 기준이 될 역이 없습니다");
        }
    }

    private boolean isNotExistBoth(Long lineId, StationRequest request) {
        return !stationDao.isExistInLine(lineId, request.getUpStation())
            && !stationDao.isExistInLine(lineId, request.getDownStation());
    }

    private Long saveDownEndStation(Long lineId, StationRequest request) {
        Long originTailId = stationDao.findTailStationByLineId(lineId);
        StationEntity newStation = new StationEntity(request.getDownStation(), lineId);
        Long newHeadId = stationDao.insert(newStation);
        stationDao.updateDistanceById(originTailId, request.getDistance());
        stationDao.updateNextStationById(originTailId, newHeadId);
        return newHeadId;
    }

    private Long saveUpEndStation(Long lineId, StationRequest request) {
        Long originHeadId = lineDao.findHeadIdById(lineId);
        StationEntity newStation = new StationEntity(request.getUpStation(), originHeadId,
            request.getDistance(),
            lineId);
        Long newHeadId = stationDao.insert(newStation);
        lineDao.updateHeadStation(lineId, newHeadId);
        return newHeadId;
    }

    private Long saveMiddle(Long lineId, StationRequest request) {
        StationEntity upStationOfNew;
        if (stationDao.isExistInLine(lineId, request.getUpStation())) {   //하행 방향으로 추가
            upStationOfNew = stationDao.findByLineIdAndName(lineId, request.getUpStation());
            validateDistance(upStationOfNew, request);
            return saveMiddleStation(lineId, request.getDownStation(), upStationOfNew,
                request.getDistance(), upStationOfNew.getDistance() - request.getDistance());
        }
        upStationOfNew = stationDao.findByNextStationId(lineId, request.getDownStation());
        validateDistance(upStationOfNew, request);
        return saveMiddleStation(lineId, request.getUpStation(), upStationOfNew,
            upStationOfNew.getDistance() - request.getDistance(), request.getDistance());
    }

    private void validateDistance(StationEntity upStationOfNew, StationRequest request) {
        if (upStationOfNew.getDistance() - request.getDistance() < MIN_DISTANCE_VALUE) {
            throw new IllegalArgumentException("추가하려는 역이 기존 목적지 역보다 멀리 있습니다");
        }
    }

    private Long saveMiddleStation(Long lineId, String newStationName, StationEntity upStationOfNew,
        int newUpStationDistance, int newDistance) {
        Long downStationOfNewId = upStationOfNew.getNext();
        StationEntity newStation = new StationEntity(newStationName, downStationOfNewId,
            newDistance, lineId);

        Long newStationId = stationDao.insert(newStation);
        stationDao.updateNextStationById(upStationOfNew.getId(), newStationId);
        stationDao.updateDistanceById(upStationOfNew.getId(), newUpStationDistance);
        return newStationId;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
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

    public void deleteStation(Long lineId, String name) {
        validateCanDelete(lineId, name);
        StationEntity deleteStation = stationDao.findByLineIdAndName(lineId, name);
        int rowNumber;
        if (lineDao.isUpEndStation(lineId, name)) {
            rowNumber = deleteUpEndStation(lineId, deleteStation);
            validateDeleteSuccess(rowNumber);
            return;
        }
        StationEntity upStation = stationDao.findByNextStationId(lineId, name);
        if (stationDao.isDownEndStation(lineId, name)) {
            rowNumber = deleteDownEndStation(deleteStation, upStation);
            validateDeleteSuccess(rowNumber);
            return;
        }
        rowNumber = deleteMiddleStation(deleteStation, upStation);
        validateDeleteSuccess(rowNumber);
    }

    private void validateCanDelete(Long lineId, String name) {
        validateStationCount(lineId);
        validateExistStation(lineId, name);
    }

    private void validateStationCount(Long lineId) {
        if (stationDao.findByLineId(lineId).size() <= MIN_STATION_COUNT) {
            throw new IllegalStateException("노선에는 최소 2개 이상의 역이 존재해야 합니다.");
        }
    }

    private void validateExistStation(Long lineId, String name) {
        stationDao.findByLineIdAndName(lineId, name);
    }

    private int deleteUpEndStation(Long lineId, StationEntity deleteStation) {
        lineDao.updateHeadStation(lineId, deleteStation.getNext());
        return stationDao.deleteById(deleteStation.getId());
    }

    private int deleteDownEndStation(StationEntity deleteStation, StationEntity upStation) {
        stationDao.updateNextStationById(upStation.getId(), EMPTY_STATION_ID);
        return stationDao.deleteById(deleteStation.getId());
    }

    private int deleteMiddleStation(StationEntity deleteStation, StationEntity upStation) {
        int newDistance = upStation.getDistance() + deleteStation.getDistance();
        stationDao.updateDistanceById(upStation.getId(), newDistance);
        stationDao.updateNextStationById(upStation.getId(), deleteStation.getNext());
        return stationDao.deleteById(deleteStation.getId());
    }

    private void validateDeleteSuccess(int rowNumber) {
        if (rowNumber != 1) {
            throw new NoSuchElementException("삭제하려는 역이 존재하지 않습니다.");
        }
    }
}

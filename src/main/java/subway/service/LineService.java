package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Station;
import subway.domain.Stations;
import subway.dto.LineCreateRequest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationRequest;
import subway.entity.LineEntity;
import subway.entity.StationEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineDao lineDao;
    private final StationDao stationDao;

    public LineService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public LineResponse saveLine(LineCreateRequest request) {
        StationRequest stationRequest = request.getStationRequest();
        StationEntity stationEntity = validateStationExistByName(stationRequest.getName());
        validateStationExistByName(stationRequest.getNextStationName());

        return addLine(request, stationEntity);
    }

    private LineResponse addLine(LineCreateRequest request, StationEntity stationEntity) {
        Station station = createStationFromStationEntity(stationEntity);
        LineRequest lineRequest = request.getLineRequest();
        LineEntity lineEntity = new LineEntity(null, lineRequest.getName(), lineRequest.getColor(), station.getId());
        Long lineId = lineDao.insert(lineEntity);

        return new LineResponse(lineId, lineEntity.getName(), lineEntity.getColor());
    }

    private StationEntity validateStationExistByName(String stationName) {
        return stationDao.findByName(stationName)
                .orElseThrow(() -> new IllegalArgumentException(stationName + "역은 등록되지 않았습니다"));
    }

    public Station createStationFromStationEntity(StationEntity stationEntity) {
        return new Station(stationEntity.getName(), new Distance(stationEntity.getDistance()));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLineResponses() {
        List<LineEntity> lineEntities = lineDao.findAllLines();
        return lineEntities.stream()
                .map(this::convertToLineResponse)
                .collect(Collectors.toList());
    }

    private LineResponse convertToLineResponse(LineEntity lineEntity) {
        return new LineResponse(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

    private Long updateLineNameAndColor(Long id, LineRequest request) {
        validateLineExistById(id);

        return lineDao.update(id, request);
    }

    private void validateLineExistById(Long id) {
        lineDao.findLineEntityById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 노선이 존재하지 않습니다"));
    }

    public void updateLine(Long id, LineCreateRequest updateRequest) {
        //line의 이름, 혹은 color를 수정하고 싶은 경우
        validateLineExistById(id);
        updateLineNameAndColor(id, updateRequest.getLineRequest());
        addStationInLine(id, updateRequest);
    }

    private void addStationInLine(Long id, LineCreateRequest updateRequest) {
        StationRequest stationRequest = updateRequest.getStationRequest();
        List<StationEntity> allStations = lineDao.findAllStationsById(id);
        Stations lineStations = createStationsFromStationEntity(allStations);

        if (isUpStationAdd(stationRequest, lineStations)) {
            addUpStation(id, stationRequest, lineStations);
        }

        if (isDownStationAdd(stationRequest, lineStations)) {
            addDownStation(id, stationRequest, lineStations);
        }
    }

    public Stations createStationsFromStationEntity(List<StationEntity> stationEntity) {
        return new Stations(stationEntity.stream()
                .map(entity -> new Station(entity.getName(), new Distance(entity.getDistance())))
                .collect(Collectors.toList()));
    }

    private void addDownStation(Long lineId, StationRequest stationRequest, Stations lineStations) {
        //upStation이 line에 존재, 새로운 downStation을 삽입하고자 하는 경우
        Station upStation = lineStations.findByName(stationRequest.getName());
        Station downStation = new Station(stationRequest.getNextStationName(), new Distance(0));

        int upStationIndex = lineStations.findIndex(upStation);
        if (upStationIndex == lineStations.getStationsSize() - 1) {
            downStation.setDistance(new Distance(upStation.getDistance().getValue() - stationRequest.getDistance()));
        }

        upStation.setDistance(new Distance(stationRequest.getDistance()));
        lineStations.addStationByIndex(upStationIndex + 1, downStation);

        StationEntity stationEntity = new StationEntity(downStation.getId(), downStation.getName(), downStation.getId(), downStation.getDistance().getValue(), lineId);
        stationDao.insert(stationEntity);
        stationDao.update(upStation.getId(), new StationEntity(
                upStation.getId(), upStation.getName(), downStation.getId(), upStation.getDistance().getValue(), lineId));
    }

    private boolean isDownStationAdd(StationRequest stationRequest, Stations lineStations) {
        return lineStations.isExistStation(stationRequest.getName()) && !lineStations.isExistStation(stationRequest.getNextStationName());
    }

    private void addUpStation(Long lineId, StationRequest stationRequest, Stations lineStations) {
        //downStation이 line에 존재, 새로운 upStation을 삽입하고자 하는 경우
        Station downStation = lineStations.findByName(stationRequest.getNextStationName());
        Station upStation = new Station(stationRequest.getName(), new Distance(stationRequest.getDistance()));
        Long nextStationId = null;
        int downStationIndex = lineStations.findIndex(downStation);

        if (downStationIndex != 0) {
            //역과 역 사이에 삽입
            Station preStation = lineStations.findUpStation(downStation);
            preStation.setDistance(new Distance(preStation.getDistance().getValue() - stationRequest.getDistance()));

            if (lineStations.findNextStationById(downStation.getId()).isPresent()) {
                nextStationId = lineStations.findNextStationById(downStation.getId()).get().getId();
            }
        } else {
            lineDao.updateHeadStation(lineId, upStation);
        }

        lineStations.addStationByIndex(downStationIndex, upStation);

        StationEntity stationEntity = new StationEntity(upStation.getId(), upStation.getName(), downStation.getId(), upStation.getDistance().getValue(), lineId);
        stationDao.insert(stationEntity);
        stationDao.update(downStation.getId(), new StationEntity(
                downStation.getId(), downStation.getName(), nextStationId, downStation.getDistance().getValue(), lineId));
    }

    private boolean isUpStationAdd(StationRequest stationRequest, Stations lineStations) {
        return !lineStations.isExistStation(stationRequest.getName())
                && lineStations.isExistStation(stationRequest.getNextStationName());
    }

    public LineResponse findById(Long id) {
        LineEntity lineEntity = validateLineResponseById(id);
        List<StationEntity> allStations = lineDao.findAllStationsById(id);
        List<String> stationsNamesInOrder = sortStations(lineEntity, allStations);

        return new LineResponse(id, lineEntity.getName(), lineEntity.getColor(), stationsNamesInOrder);
    }

    private List<String> sortStations(LineEntity lineEntity, List<StationEntity> allStations) {
        List<String> stationsNamesInOrder = new ArrayList<>();
        Long targetId = lineEntity.getHeadStationId();

        for (int i = 0; i < allStations.size(); i++) {
            StationEntity stationEntity = allStations.get(i);
            if (targetId == stationEntity.getId()) {
                stationsNamesInOrder.add(stationEntity.getName());
                targetId = stationEntity.getNextStationId();
                i = -1;
            }
        }
        return stationsNamesInOrder;
    }

    private LineEntity validateLineResponseById(Long id) {
        LineEntity lineEntity = lineDao.findLineEntityById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾고자하는 id에 해당하는 LineResponse를 생성할 수 없습니다."));
        return lineEntity;
    }

    public Long deleteLineById(Long id) {
        validateLineExistById(id);

        return lineDao.remove(id);
    }
}

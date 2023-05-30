package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Stations;
import subway.dto.LineCreateRequest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationRequest;
import subway.entity.LineEntity;
import subway.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class LineService {

    private final LineDao lineDao;
    private final StationDao stationDao;

    //TODO: 맨 위는 dao에서 entity가져와서 domain에서 검증(ex.생성자 혹은 method), 중간에서 검증(domain 비즈니스 검증. 이를테면 null check).... ->
    //TODO: entity는 중간 매개체. domain으로 변환할 때 검증하면 됨.
    public LineService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public LineResponse saveLine(LineCreateRequest request) {
        StationRequest stationRequest = request.getStationRequest();
        validateStationsExist(stationRequest.getName(), stationRequest.getNextStationName());
        StationEntity stationEntity = stationDao.findByName(stationRequest.getName()).get();

        return addLine(request, stationEntity);
    }

    private void validateStationsExist(String name, String nextStationName) {
        validateStationExistByName(name);
        validateStationExistByName(nextStationName);
    }

    private LineResponse addLine(LineCreateRequest request, StationEntity stationEntity) {
        Station station = stationEntity.convertToStation();
        Station nextStation = stationDao.findStationEntityById(stationEntity.getNextStationId()).get().convertToStation();

        LineRequest lineRequest = request.getLineRequest();
        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), new Stations(List.of(station, nextStation)));
        LineEntity lineEntity = new LineEntity(null, line.getName(), line.getColor(), line.getId());
        Long lineId = lineDao.insert(lineEntity);

        return new LineResponse(lineId, lineEntity.getName(), lineEntity.getColor());
    }

    private void validateStationExistByName(String stationName) {
        stationDao.findByName(stationName)
                .orElseThrow(() -> new IllegalArgumentException(stationName + "역은 등록되지 않았습니다"));
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

    public void updateLine(Long id, LineCreateRequest updateRequest) {
        //line의 이름, 혹은 color를 수정하고 싶은 경우
        validateLineExistById(id);
        updateLineNameAndColor(id, updateRequest.getLineRequest());
        addStationInLine(id, updateRequest);
    }

    private void validateLineExistById(Long id) {
        lineDao.findLineEntityById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 노선이 존재하지 않습니다"));
    }

    private Long updateLineNameAndColor(Long id, LineRequest request) {
        validateLineExistById(id);

        return lineDao.update(id, request);
    }

    private void addStationInLine(Long id, LineCreateRequest updateRequest) {
        StationRequest stationRequest = updateRequest.getStationRequest();
        List<StationEntity> stationsEntities = lineDao.findAllStationsById(id);
        Stations lineStations = StationEntity.convertToStations(stationsEntities);

        Station station = new Station(stationRequest.getName(), new Distance(0));
        Station nextStation = new Station(stationRequest.getNextStationName(), new Distance(0));
        Distance distance = new Distance(stationRequest.getDistance());

        List<Station> stations = lineStations.addStationInLine(station, nextStation, distance);
        addAllFromStations(id, station, stations);
    }

    private void addAllFromStations(Long id, Station station, List<Station> stations) {
        lineDao.updateHeadStation(id, stations.get(0));
        stationDao.removeAllByLineId(id);
        for (int i = 0; i < stations.size(); i++) {
            StationEntity stationEntity = new StationEntity(station.getId(), station.getName(), null, station.getDistance().getValue(), id);
            stationDao.insert(stationEntity);
        }
    }

    public LineResponse findById(Long id) {
        LineEntity lineEntity = validateLineResponseById(id);
        List<StationEntity> allStations = lineDao.findAllStationsById(id);
        List<String> stationNames = StationEntity.convertToStations(allStations).getStationNames();

        return new LineResponse(id, lineEntity.getName(), lineEntity.getColor(), stationNames);
    }

    private LineEntity validateLineResponseById(Long id) {
        return lineDao.findLineEntityById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾고자하는 id에 해당하는 LineResponse를 생성할 수 없습니다."));
    }


    public Long deleteLineById(Long id) {
        validateLineExistById(id);

        return lineDao.remove(id);
    }
}

package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Lines;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineDao lineDao;
    private final Stations stations;
    private final Lines lines;
    private final StationDao stationDao;

    public LineService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.stations = new Stations(new ArrayList<>());
        this.lines = new Lines(new ArrayList<>());
    }

    public LineResponse addLine(LineCreateRequest request) {
        StationRequest stationRequest = request.getStationRequest();
        Station station = stations.findByName(stationRequest.getName());
        Station nextStation = stations.findByName(stationRequest.getNextStationName());

        LineRequest lineRequest = request.getLineRequest();
        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), new Stations(new ArrayList<>(List.of(station, nextStation))));
        lines.addLine(line);

        //entity 생성
        LineEntity lineEntity = new LineEntity(line.getId(), lineRequest.getName(), lineRequest.getColor(), station.getId());

        Long lineId = lineDao.insert(lineEntity);
        return new LineResponse(lineId, line.getName(), line.getColor());
    }

    public List<LineResponse> findLineResponses() {
        List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(this::convertToLineResponse)
                .collect(Collectors.toList());
    }

    private LineResponse convertToLineResponse(LineEntity lineEntity) {
        //line에 있는 stations들의 정보를 다 가져와
        //
        return new LineResponse(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

    public Line updateLineNameAndColor(Long id, LineRequest request) {
        Line line = lines.getLines().get(id.intValue() - 1);
        line.setName(request.getName());
        line.setColor(request.getColor());
        lineDao.update(id, request);

        return line;
    }

    public void updateLine(Long id, LineCreateRequest updateRequest) {
        //line의 이름, 혹은 color를 수정하고 싶은 경우
        Line line = updateLineNameAndColor(id, updateRequest.getLineRequest());

        StationRequest stationRequest = updateRequest.getStationRequest();
        Stations lineStations = line.getStations();


        if (isUpStationAdd(stationRequest, lineStations)) {
            addUpStation(line, stationRequest, lineStations);
        }

        if (isDownStationAdd(stationRequest, lineStations)) {
            addDownStation(line, stationRequest, lineStations);
        }
    }

    private void addDownStation(Line line, StationRequest stationRequest, Stations lineStations) {
        //upStation이 line에 존재, 새로운 downStation을 삽입하고자 하는 경우
        Station upStation = lineStations.findByName(stationRequest.getName());
        Station downStation = new Station(stationRequest.getNextStationName(), new Distance(0));

        int upStationIndex = lineStations.findIndex(upStation);
        if (upStationIndex == lineStations.getStationsSize() - 1) {
            downStation.setDistance(new Distance(upStation.getDistance().getValue() - stationRequest.getDistance()));
        }

        upStation.setDistance(new Distance(stationRequest.getDistance()));
        lineStations.addStationByIndex(upStationIndex + 1, downStation);

        StationEntity stationEntity = new StationEntity(downStation.getId(), downStation.getName(), downStation.getId(), downStation.getDistance().getValue(), line.getId());
        stationDao.insert(stationEntity);
        stationDao.update(upStation.getId(), new StationEntity(
                upStation.getId(), upStation.getName(), downStation.getId(), upStation.getDistance().getValue(), line.getId()));
    }

    private boolean isDownStationAdd(StationRequest stationRequest, Stations lineStations) {
        return lineStations.isExistStation(stationRequest.getName()) && !lineStations.isExistStation(stationRequest.getNextStationName());
    }

    private void addUpStation(Line line, StationRequest stationRequest, Stations lineStations) {
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
        }

        lineStations.addStationByIndex(downStationIndex, upStation);

        StationEntity stationEntity = new StationEntity(upStation.getId(), upStation.getName(), downStation.getId(), upStation.getDistance().getValue(), line.getId());
        stationDao.insert(stationEntity);
        stationDao.update(downStation.getId(), new StationEntity(
                downStation.getId(), downStation.getName(), nextStationId, downStation.getDistance().getValue(), line.getId()));
    }

    private boolean isUpStationAdd(StationRequest stationRequest, Stations lineStations) {
        return !lineStations.isExistStation(stationRequest.getName()) && lineStations.isExistStation(stationRequest.getNextStationName());
    }

    public LineResponse createLineResponseById(Long id) {
        Optional<LineEntity> line = lineDao.findLineEntityById(id);
        if (line.isPresent()) {
            LineEntity lineEntity = line.get();
            return new LineResponse(id, lineEntity.getName(), lineEntity.getColor());
        }
        throw new IllegalArgumentException("찾고자하는 id에 해당하는 LineResponse를 생성할 수 없습니다.");
    }

    public Stations getStations() {
        return stations;
    }

    public Lines getLines() {
        return lines;
    }
}

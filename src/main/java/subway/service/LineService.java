package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Station;
import subway.domain.Stations;
import subway.dto.LineCreateRequest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationRequest;
import subway.entity.LineEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineDao lineDao;
    private final Stations stations;
    private final Lines lines;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
        this.stations = new Stations(new ArrayList<>());
        this.lines = new Lines(new ArrayList<>());
    }

    public LineResponse addLine(LineCreateRequest request) {
        StationRequest stationRequest = request.getStationRequest();
        Station station = stations.findByName(stationRequest.getName());
        Station nextStation = stations.findByName(stationRequest.getNextStationName());

        stations.addStation(station);
        stations.addStation(nextStation);

        LineRequest lineRequest = request.getLineRequest();
        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), stations);
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
        // LineEntity를 LineResponse로 변환하는 로직 작성
        // 필요한 정보를 추출하여 LineResponse 객체를 생성하고 반환
        return new LineResponse(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }

    public Stations getStations() {
        return stations;
    }

    public Lines getLines() {
        return lines;
    }
}

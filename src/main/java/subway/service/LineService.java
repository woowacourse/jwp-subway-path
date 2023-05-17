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

    public Stations getStations() {
        return stations;
    }

    public Lines getLines() {
        return lines;
    }
}

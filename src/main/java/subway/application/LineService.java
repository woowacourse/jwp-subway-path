package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.line.Direction;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.RegisterStationRequest;
import subway.dto.InitStationsRequest;
import subway.dto.StationResponse;
import subway.exception.AlreadyExistLineException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new AlreadyExistLineException();
        }
        Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public void registerInitStations(final String lineName, final InitStationsRequest initStationsRequest) {
        Line line = lineRepository.findByName(lineName);

        Station leftStation = stationRepository.findByName(initStationsRequest.getLeftStationName());
        Station rightStation = stationRepository.findByName(initStationsRequest.getRightStationName());
        line.initStations(leftStation, rightStation, initStationsRequest.getDistance());

        lineRepository.save(line);
    }

    public void registerStation(String lineName, RegisterStationRequest registerStationRequest) {
        Line line = lineRepository.findByName(lineName);
        if (line.getSections().isEmpty()) {
            throw new IllegalArgumentException("두 개의 역이 초기화 되지 않은 노선에 새로운 역을 추가할 수 없습니다.");
        }
        Station newStation = stationRepository.findByName(registerStationRequest.getNewStationName());
        Station baseStation = stationRepository.findByName(registerStationRequest.getBaseStationName());
        line.addStation(newStation, baseStation, Direction.of(registerStationRequest.getDirection()), registerStationRequest.getDistance());
        lineRepository.save(line);
    }

    public void deleteStation(final String lineName, final String stationName) {
        Line line = lineRepository.findByName(lineName);

        Station deleteStation = stationRepository.findByName(stationName);
        line.deleteStation(deleteStation);

        lineRepository.save(line);
    }

    public LineResponse findLineResponseByName(final String name) {
        Line line = lineRepository.findByName(name);
        List<StationResponse> stationResponses = line.getStations().getStations().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toUnmodifiableList());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }

    public List<LineResponse> findAll() {
        List<Line> lineEntities = lineRepository.findAll();
        return lineEntities.stream()
                .map(line -> findLineResponseByName(line.getName()))
                .collect(Collectors.toUnmodifiableList());
    }

    public void deleteLineById(Long lineId) {
        if (lineRepository.registeredStationsById(lineId)) {
            throw new IllegalArgumentException("노선에 역이 등록되어 있으면 삭제할 수 없습니다.");
        }
        lineRepository.deleteById(lineId);
    }

    public LineResponse updateLine(Long lineId, LineRequest request) {
        Line line = lineRepository.findById(lineId);
        Line persistLine = lineRepository.save(new Line(line.getId(), request.getName(), request.getColor(), line.getSections(), line.getUpBoundStation(), line.getDownBoundStation()));
        return LineResponse.of(persistLine);
    }

    public LineResponse findLineResponseById(Long id) {
        Line line = lineRepository.findById(id);
        List<StationResponse> stationResponses = line.getStations().getStations().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toUnmodifiableList());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }
}

package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.line.Direction;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.dto.InitStationsRequest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.RegisterStationRequest;
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

    public void registerInitStationsByLineId(final Long lineId, final InitStationsRequest initStationsRequest) {
        Line line = lineRepository.findById(lineId);

        Station leftStation = stationRepository.findById(initStationsRequest.getLeftStationId());
        Station rightStation = stationRepository.findById(initStationsRequest.getRightStationId());
        line.initStations(leftStation, rightStation, initStationsRequest.getDistance());

        lineRepository.save(line);
    }

    public void registerStationByLineId(Long lineId, RegisterStationRequest registerStationRequest) {
        Line line = lineRepository.findById(lineId);
        Station newStation = stationRepository.findById(registerStationRequest.getNewStationId());
        Station baseStation = stationRepository.findById(registerStationRequest.getBaseStationId());
        line.addStation(newStation, baseStation, Direction.of(registerStationRequest.getDirection()), registerStationRequest.getDistance());
        lineRepository.save(line);
    }

    public void deleteStation(final Long lineId, final Long stationId) {
        Line line = lineRepository.findById(lineId);

        Station deleteStation = stationRepository.findById(stationId);
        line.deleteStation(deleteStation);

        lineRepository.save(line);
    }

    public List<LineResponse> findAll() {
        List<Line> lineEntities = lineRepository.findAll();
        return lineEntities.stream()
                .map(line -> findLineResponseById(line.getId()))
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

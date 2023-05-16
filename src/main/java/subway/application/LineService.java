package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.RegisterInnerStationRequest;
import subway.dto.RegisterLastStationRequest;
import subway.dto.RegisterStationsRequest;
import subway.dto.StationResponse;
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
        Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public void registerInitStations(final String lineName, final RegisterStationsRequest registerStationsRequest) {
        Line line = lineRepository.findByName(lineName);

        Station leftStation = stationRepository.findByName(registerStationsRequest.getLeftStationName());
        Station rightStation = stationRepository.findByName(registerStationsRequest.getRightStationName());
        line.initStations(leftStation, rightStation, registerStationsRequest.getDistance());

        lineRepository.save(line);
    }

    public void registerLastStation(String lineName, RegisterLastStationRequest registerLastStationRequest) {
        Line line = lineRepository.findByName(lineName);

        Station baseStation = stationRepository.findByName(registerLastStationRequest.getBaseStationName());
        Station newStation = stationRepository.findByName(registerLastStationRequest.getNewStationName());

        line.addLastStation(baseStation, newStation, registerLastStationRequest.getDistance());
        lineRepository.save(line);
    }

    public void registerInnerStation(final String lineName, final RegisterInnerStationRequest registerInnerStationRequest) {
        Line line = lineRepository.findByName(lineName);

        Station leftBaseStation = stationRepository.findByName(registerInnerStationRequest.getLeftBaseStationName());
        Station rightBaseStation = stationRepository.findByName(registerInnerStationRequest.getRightBaseStationName());
        Station newStation = stationRepository.findByName(registerInnerStationRequest.getNewStationName());

        line.addInnerStation(leftBaseStation, registerInnerStationRequest.getLeftDistance(), rightBaseStation, registerInnerStationRequest.getRightDistance(), newStation);
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
}

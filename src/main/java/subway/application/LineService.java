package subway.application;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import subway.domain.line.Line;
import subway.domain.line.Station;
import subway.domain.Subway;
import subway.dto.line.LineCreateRequest;
import subway.dto.line.LineCreateResponse;
import subway.dto.line.LineSelectResponse;
import subway.dto.line.LinesSelectResponse;
import subway.dto.station.StationSaveRequest;
import subway.dto.station.StationSelectResponse;
import subway.exception.station.AlreadyExistStationException;
import subway.exception.station.StationNotFoundException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    private LineService(LineRepository lineRepository,
                       StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineCreateResponse saveLine(LineCreateRequest request) {
        Line line = request.toDomain();
        Long saveId = lineRepository.save(line);
        return new LineCreateResponse(saveId);
    }

    public LinesSelectResponse findAllLine() {
        final List<Line> lines = lineRepository.findAll();

        return LinesSelectResponse.from(lines);
    }

    public LineSelectResponse getStationsByLineId(Long lineId) {
        Line line = lineRepository.findById(lineId);
        return LineSelectResponse.from(line);
    }

    public StationSelectResponse addStation(Long lineId, StationSaveRequest stationRequest) {
        Subway subway = new Subway(lineRepository.findAll());
        final Line line = subway.findLineById(lineId);

        List<Optional<String>> requestStations = extractNullableStation(stationRequest, line);
        final String newStationName = extractStationNameToAdd(requestStations);

        final Station saveStation = stationRepository.insert(newStationName);

        subway.addStation(
                line.getName(),
                stationRequest.getSourceStation(),
                stationRequest.getTargetStation(),
                stationRequest.getDistance()
        );
        saveUpdatedLine(subway, line.getName(), lineId);
        return new StationSelectResponse(saveStation.getId(), saveStation.getName());
    }

    private List<Optional<String>> extractNullableStation(
            final StationSaveRequest stationRequest,
            final Line lineByName)
    {
        final List<Station> stations = lineByName.getStations();
        List<Optional<String>> requestStations = new ArrayList<>();
        requestStations.add(filterNonExistStation(stations, stationRequest.getSourceStation()));
        requestStations.add(filterNonExistStation(stations, stationRequest.getTargetStation()));
        return requestStations;
    }

    private String extractStationNameToAdd(final List<Optional<String>> requestStations) {
        return requestStations.stream()
                .filter(Optional::isPresent)
                .flatMap(Optional::stream)
                .findFirst()
                .orElseThrow(AlreadyExistStationException::new);
    }

    private Optional<String> filterNonExistStation(List<Station> stations, String requestStationName) {
        if (noneMatchStationName(stations, requestStationName)) {
            return Optional.ofNullable(requestStationName);
        }

        return Optional.empty();
    }

    private boolean noneMatchStationName(final List<Station> stations, final String requestStationName) {
        return stations.stream()
                .noneMatch(station -> station.getName().equals(requestStationName));
    }

    private void saveUpdatedLine(Subway subway, String lineName, Long lineId) {
        Line updatedLine = subway.getLines().stream()
                .filter(line -> line.isSameName(lineName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 Line 이름입니다."));
        lineRepository.updateLine(lineId, updatedLine);
    }

    public void deleteStation(final Long lineId, final Long stationId) {
        final Subway subway = new Subway(lineRepository.findAll());
        final Line line = subway.findLineById(lineId);
        final Station findStation = stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException("삭제하려는 역이 존재하지 않습니다."));

        line.removeStation(findStation);
        lineRepository.deleteStationByLineIdAndStationId(lineId, stationId);

        if (line.getStations().isEmpty()) {
            lineRepository.deleteLineById(lineId);
        }

        if (subway.notContainsStation(findStation)) {
            stationRepository.deleteById(stationId);
        }
    }
}

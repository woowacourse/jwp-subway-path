package subway.application;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Subway;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.dto.StationSaveRequest;
import subway.dto.StationsResponse;
import subway.entity.LineEntity;
import subway.entity.StationEntity;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.repository.dao.LineDao;

@Service
public class LineService {

    private final LineDao lineDao;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineDao lineDao, LineRepository lineRepository,
                       StationRepository stationRepository) {
        this.lineDao = lineDao;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = request.toDomain();
        Long saveId = lineRepository.save(line);
        return new LineResponse(saveId);
    }

    public List<LineResponse> findLineResponses() {
        List<LineEntity> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<LineEntity> findLines() {
        return lineDao.findAll();
    }

    public StationsResponse getStationsByLineId(Long lineId) {
        Line line = lineRepository.findById(lineId);
        return StationsResponse.from(line.getStations());
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new LineEntity(id, lineUpdateRequest.getLineName()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

    public StationResponse addStation(Long lineId, StationSaveRequest stationRequest) {
        Subway subway = new Subway(lineRepository.findAllLine());
        LineEntity lineEntity = lineDao.findById(lineId);
        Line lineByName = subway.findLineByName(lineEntity.getName());

        List<Optional<String>> requestStations = extractNullableStation(stationRequest, lineByName);
        final String newStationName = requestStations.stream()
                .filter(Optional::isPresent)
                .flatMap(Optional::stream)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("추가하려는 역은 이미 노선에 존재합니다."));

        stationRepository.save(new StationEntity(newStationName));

        subway.addStation(lineEntity.getName(),
                stationRequest.getSourceStation(),
                stationRequest.getTargetStation(),
                stationRequest.getDistance());
        saveUpdatedLine(subway, lineEntity.getName(), lineId);
        return StationResponse.of(new StationEntity(newStationName));
    }

    private List<Optional<String>> extractNullableStation(final StationSaveRequest stationRequest, final Line lineByName) {
        final List<Station> stations = lineByName.getStations();
        List<Optional<String>> requestStations = new ArrayList<>();
        requestStations.add(filterNonExistStation(stations, stationRequest.getSourceStation()));
        requestStations.add(filterNonExistStation(stations, stationRequest.getTargetStation()));
        return requestStations;
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
}

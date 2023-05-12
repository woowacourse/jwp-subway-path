package subway.application;

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

    public LineResponse findLineResponseById(Long id) {
        return null;
    }

    public StationsResponse getStationByLineId(Long id) {
        Line line = lineRepository.findById(id);
        return StationsResponse.from(line.getStations());
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new LineEntity(id, lineUpdateRequest.getLineName()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

    public StationResponse addStation(Long lineId, StationSaveRequest stationRequest) {
        Subway subway = new Subway(lineRepository.findAll());
        LineEntity byId = lineDao.findById(lineId);
        Line lineByName = subway.findLineByName(byId.getName());
        Optional<Station> newStation = lineByName.getStations()
                .stream()
                .filter(station ->
                        !station.isSameName(new Station(stationRequest.getSourceStation())) &&
                                !station.isSameName(new Station(stationRequest.getTargetStation())))
                .findFirst();

        String newStationName = newStation.get().getName();
        stationRepository.save(new StationEntity(newStationName));

        subway.addStation(byId.getName(), stationRequest.getSourceStation(), stationRequest.getTargetStation(),
                stationRequest.getDistance());
        saveUpdatedLine(subway, byId.getName(), lineId);
        return StationResponse.of(new StationEntity(newStationName));
    }

    private void saveUpdatedLine(Subway subway, String lineName, Long lineId) {
        Line updatedLine = subway.getLines().stream()
                .filter(line -> line.isSameName(lineName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException());
        lineRepository.updateLine(lineId, updatedLine);
    }
}

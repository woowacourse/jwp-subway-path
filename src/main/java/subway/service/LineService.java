package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineDirection;
import subway.dto.LineRequest;
import subway.dto.StationInsertRequest;
import subway.exception.DuplicatedLineNameException;
import subway.exception.LineNotFoundException;
import subway.exception.NoSuchStationException;
import subway.exception.StationNotFoundException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.service.dto.CreateLineServiceRequest;
import subway.service.dto.InsertStationServiceRequest;

@Service
public class LineService {

    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public Long create(CreateLineServiceRequest request) {
        validate(request.getName());
        validate(request.getUpStationId());
        validate(request.getDownStationId());

        final Line line = Line.of(request.getName(), request.getColor(),
                request.getUpStationId(), request.getDownStationId(), request.getDistance());

        return lineRepository.create(line);
    }

    private void validate(String lineName) {
        lineRepository.findByName(lineName)
                .ifPresent(lineWithDuplicatedName -> {
                    throw new DuplicatedLineNameException(lineName);
                });
    }

    private void validate(Long stationId) {
        stationRepository.findById(stationId)
                .orElseThrow(StationNotFoundException::new);
    }

    @Transactional
    public void insertStation(InsertStationServiceRequest request) {
        validate(request.getStationId());
        validate(request.getAdjacentStationId());

        Line line = findLineById(request.getLineId());
        addStation(request, line);
        lineRepository.update(line);
    }


    private void addStation(
            InsertStationServiceRequest stationInsertRequest,
            Line line
    ) {
        Long stationId = stationInsertRequest.getStationId();
        Long adjacentStationId = stationInsertRequest.getAdjacentStationId();
        LineDirection direction = LineDirection.valueOf(stationInsertRequest.getDirection());
        int distance = stationInsertRequest.getDistance();

        if (direction == LineDirection.UP) {
            line.addStationUpperFrom(stationId, adjacentStationId, distance);
        }
        line.addStationDownFrom(stationId, adjacentStationId, distance);
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
    }

    public void deleteStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        if (!line.contains(stationId)) {
            throw new NoSuchStationException();
        }

        if (!line.canDeleteStation()) {
            lineRepository.deleteById(line.getId());
            return;
        }

        line.deleteStation(stationId);
        lineRepository.update(line);
    }

    public List<Line> findAll() {
        return lineRepository.findAll();
    }
}

package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineDirection;
import subway.domain.Station;
import subway.domain.dto.InsertionResult;
import subway.dto.LineRequest;
import subway.dto.StationInsertRequest;
import subway.exception.DuplicatedLineNameException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LineService {

    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public Long create(final LineRequest lineRequest) {
        final Station upStation = findStationById(lineRequest.getUpStationId());
        final Station downStation = findStationById(lineRequest.getDownStationId());

        final Line line = Line.of(lineRequest.getName(), lineRequest.getColor(),
                upStation.getId(), downStation.getId(), lineRequest.getDistance());

        lineRepository.findByName(line.getName()).ifPresent(lineWithSameName -> {
            throw new DuplicatedLineNameException(line.getName());
        });

        return lineRepository.create(line);
    }

    @Transactional
    public void insertStation(final StationInsertRequest stationInsertRequest) {
        findStationById(stationInsertRequest.getStationId());
        findStationById(stationInsertRequest.getAdjacentStationId());

        final Line line = findLineById(stationInsertRequest.getLineId());
        final InsertionResult insertionResult = insertStationAndReturnEdgesToSave(stationInsertRequest, line);

        lineRepository.insertStationEdge(line, insertionResult.getInsertedEdge());
        if (insertionResult.getUpdatedEdge() != null) {
            lineRepository.updateStationEdge(line, insertionResult.getUpdatedEdge());
        }
    }


    private InsertionResult insertStationAndReturnEdgesToSave(
            final StationInsertRequest stationInsertRequest,
            final Line line
    ) {
        final Long stationId = stationInsertRequest.getStationId();
        final Long adjacentStationId = stationInsertRequest.getAdjacentStationId();
        final LineDirection direction = LineDirection.valueOf(stationInsertRequest.getDirection());
        final int distance = stationInsertRequest.getDistance();

        if (direction == LineDirection.UP) {
            return line.insertUpStation(stationId, adjacentStationId, distance);
        }
        return line.insertDownStation(stationId, adjacentStationId, distance);
    }

    public Line findLineById(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
    }

    private Station findStationById(final Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(StationNotFoundException::new);
    }

    public void deleteStation(final Long lineId, final Long stationId) {
        final Line line = findLineById(lineId);
        if (line.size() == 2 && line.contains(stationId)) {
            lineRepository.deleteById(line.getId());
            return;
        }
        Optional.ofNullable(line.deleteStation(stationId))
                .ifPresent(stationEdge -> lineRepository.updateStationEdge(line, stationEdge));
        lineRepository.deleteStation(line, stationId);
    }

    public List<Line> findAll() {
        return lineRepository.findAll();
    }
}

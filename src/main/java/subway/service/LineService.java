package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineDirection;
import subway.domain.Station;
import subway.exception.DuplicatedLineNameException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.ui.dto.LineRequest;
import subway.ui.dto.StationInsertRequest;

import java.util.List;

@Service
public class LineService {

    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
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
        validateIsStationExist(List.of(stationInsertRequest.getStationId(), stationInsertRequest.getAdjacentStationId()));

        final Line line = findLineById(stationInsertRequest.getLineId());
        insertStationInLine(stationInsertRequest, line);

        lineRepository.updateStationEdges(line);
    }

    private void validateIsStationExist(final List<Long> stationIds) {
        final List<Station> stations = stationRepository.findById(stationIds);
        if (stations.size() != stationIds.size()) {
            throw new StationNotFoundException();
        }
    }


    private void insertStationInLine(
            final StationInsertRequest stationInsertRequest,
            final Line line
    ) {
        final Long stationId = stationInsertRequest.getStationId();
        final Long adjacentStationId = stationInsertRequest.getAdjacentStationId();
        final LineDirection direction = LineDirection.valueOf(stationInsertRequest.getDirection());
        final int distance = stationInsertRequest.getDistance();

        line.insertStation(stationId, adjacentStationId, direction, distance);
    }

    @Transactional
    public Line findLineById(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
    }

    private Station findStationById(final Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(StationNotFoundException::new);
    }

    @Transactional
    public void deleteStation(final Long lineId, final Long stationId) {
        final Line line = findLineById(lineId);
        line.deleteStation(stationId);
        lineRepository.updateStationEdges(line);
        if (line.size() == 0) {
            lineRepository.deleteById(line.getId());
        }
    }

    @Transactional
    public List<Line> findAll() {
        return lineRepository.findAll();
    }
}
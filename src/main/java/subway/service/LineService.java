package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.LineDirection;
import subway.domain.station.Station;
import subway.domain.Subway;
import subway.exception.LineNotFoundException;
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
        final Subway subway = loadSubwayFromRepository();
        final Station upStation = subway.getStation(lineRequest.getUpStationId());
        final Station downStation = subway.getStation(lineRequest.getDownStationId());

        final Line line = Line.of(lineRequest.getName(), lineRequest.getColor(),
                upStation.getId(), downStation.getId(), lineRequest.getDistance());
        subway.addLine(line);

        return lineRepository.create(line);
    }

    @Transactional
    public void insertStation(final StationInsertRequest stationInsertRequest) {
        final Subway subway = loadSubwayFromRepository();
        subway.insertStationToLine(
                stationInsertRequest.getLineId(),
                stationInsertRequest.getStationId(),
                stationInsertRequest.getAdjacentStationId(),
                LineDirection.valueOf(stationInsertRequest.getDirection()),
                stationInsertRequest.getDistance()
        );
        final Line insertedLine = subway.getLine(stationInsertRequest.getLineId());

        lineRepository.updateStationEdges(insertedLine);
    }

    private Subway loadSubwayFromRepository() {
        final List<Line> lines = lineRepository.findAll();
        final List<Station> stations = stationRepository.findAll();
        return Subway.of(lines, stations);
    }

    @Transactional
    public Line findLineById(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
    }

    @Transactional
    public void deleteStation(final Long lineId, final Long stationId) {
        final Subway subway = loadSubwayFromRepository();
        subway.removeStationFromLine(lineId, stationId);
        final Line line = subway.getLine(lineId);
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

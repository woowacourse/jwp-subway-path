package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.StationEdge;
import subway.dto.LineRequest;
import subway.exception.StationNotFoundException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
public class LineService {

    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public Long create(LineRequest lineRequest) {
        final Station upStation = stationRepository.findById(lineRequest.getUpStationId())
                .orElseThrow(StationNotFoundException::new);
        final Station downStation = stationRepository.findById(lineRequest.getDownStationId())
                .orElseThrow(StationNotFoundException::new);
        final StationEdge stationEdge = new StationEdge(upStation, downStation, lineRequest.getDistance());
        final Line line = Line.of(lineRequest.getName(), lineRequest.getColor(), stationEdge);
        return lineRepository.create(line);
    }
}

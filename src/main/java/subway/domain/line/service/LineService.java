package subway.domain.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.domain.Line;
import subway.domain.line.domain.repository.LineRepository;
import subway.domain.line.presentation.dto.LineRequest;
import subway.domain.line.presentation.dto.LineStationResponse;
import subway.domain.station.domain.Station;
import subway.domain.station.domain.repository.StationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Long insert(final Line line) {
        return lineRepository.insert(line);
    }

    public LineStationResponse findAllByIdAsc(final Long lineId) {
        Line line = lineRepository.findAllStationsByLineId(lineId);
        Station finalUpStation = stationRepository.findFinalUpStation(lineId);
        List<Station> stations = line.getSortedStations(finalUpStation);
        return LineStationResponse.from(line, stations);
    }

    @Transactional
    public void update(final Long id, final LineRequest request) {
        Line line = lineRepository.findById(id);
        line.updateInfo(request.getName(), request.getColor());
        lineRepository.updateById(id, line);
    }

    @Transactional
    public void deleteById(final Long id) {
        lineRepository.deleteById(id);
    }

}

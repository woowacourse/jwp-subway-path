package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.domain.repository.LineRepository;
import subway.line.presentation.dto.LineRequest;
import subway.line.presentation.dto.LineStationResponse;
import subway.section.domain.Sections;
import subway.section.domain.repository.SectionRepository;
import subway.station.domain.Station;
import subway.station.domain.repository.StationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public Long insert(final Line line) {
        return lineRepository.insert(line);
    }

    public LineStationResponse findAllByIdAsc(final Long lineId) {
        Line line = lineRepository.findById(lineId);
        Sections sections = sectionRepository.findByLineId(lineId);
        Station finalUpStation = stationRepository.findFinalUpStation(lineId);
        List<Station> stations = sections.getStationsAsc(finalUpStation);
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

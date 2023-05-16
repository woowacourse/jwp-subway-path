package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.exception.LineAlreadyExistException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;
import subway.ui.line.dto.AddStationToLineRequest;
import subway.ui.line.dto.LineCreateRequest;

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
    public Line createNewLine(final LineCreateRequest request) {
        final Station upStation = stationRepository.save(new Station(request.getUpStation()));
        final Station downStation = stationRepository.save(new Station(request.getDownStation()));
        final Line line = new Line(request.getLineName());
        if (lineRepository.findLineByName(line).isPresent()) {
            throw new LineAlreadyExistException(request.getLineName());
        }
        final Section section = new Section(upStation, downStation, new Distance(request.getDistance()));

        return lineRepository.saveWithSections(new Line(line.getName(), new Sections(List.of(section))));
    }

    @Transactional
    public Line addStationToLine(final Long lineId, final AddStationToLineRequest request) {
        final Station existStation = stationRepository.findById(request.getExistStationId())
                .orElseThrow(() -> new StationNotFoundException(request.getExistStationId()));
        final Station newStation = stationRepository.save(new Station(request.getNewStationName()));
        final Line line = lineRepository.findLineWithSectionsByLineId(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));

        line.addSection(existStation, newStation, request.getDirection().getDirectionStrategy(), new Distance(request.getDistance()));
        sectionRepository.deleteAllByLineId(lineId);
        sectionRepository.insertAllByLineId(lineId, line.getSections());

        return line;
    }

    public Line findLineById(final Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
    }

    public List<Station> getStations(final Long lineId) {
        final Line findLine = lineRepository.findLineWithSectionsByLineId(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));

        return findLine.stations();
    }

    public List<Line> findAllLines() {

        return lineRepository.findAllWithSections();
    }

    @Transactional
    public void deleteStationFromLine(final Long lineId, final Long stationId) {
        final Line line = lineRepository.findLineWithSectionsByLineId(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
        final Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(stationId));

        line.delete(station);
        sectionRepository.deleteAllByLineId(lineId);
        sectionRepository.insertAllByLineId(lineId, line.getSections());
    }
}

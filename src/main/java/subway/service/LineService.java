package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.request.LineCreateRequest;
import subway.dto.request.StationAddToLineRequest;
import subway.dto.request.StationDeleteFromLineRequest;
import subway.exception.DuplicateLineException;
import subway.exception.NotFoundLineException;
import subway.exception.NotFoundStationException;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.StationRepository;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository,
                       final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public Long create(final LineCreateRequest request) {
        if (lineRepository.findByName(request.getLineName()).isPresent()) {
            throw new DuplicateLineException(request.getLineName());
        }
        final Station up = findStationByName(request.getUpTerminalName());
        final Station down = findStationByName(request.getDownTerminalName());
        final Sections sections = new Sections(new Section(up, down, request.getDistance()));
        return lineRepository.save(new Line(request.getLineName(), sections, request.getAdditionalFee()));
    }

    private Station findStationByName(final String name) {
        return stationRepository.findByName(name)
                .orElseThrow(() -> new NotFoundStationException(name));
    }

    public void addStation(final StationAddToLineRequest request) {
        final Line line = findLineByName(request.getLineName());
        final Station up = findStationByName(request.getUpStationName());
        final Station down = findStationByName(request.getDownStationName());
        final Section section = new Section(up, down, request.getDistance());
        line.addSection(section);
        lineRepository.update(line);
    }

    private Line findLineByName(final String name) {
        return lineRepository.findByName(name)
                .orElseThrow(() -> new NotFoundLineException(name));
    }

    public void removeStation(final StationDeleteFromLineRequest request) {
        final Line line = findLineByName(request.getLineName());
        final Station station = findStationByName(request.getDeleteStationName());
        line.removeStation(station);
        if (line.getSections().isEmpty()) {
            lineRepository.delete(line);
            return;
        }
        lineRepository.update(line);
    }
}

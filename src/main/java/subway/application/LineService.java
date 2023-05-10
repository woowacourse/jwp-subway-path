package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.AddStationToLineCommand;
import subway.application.dto.DeleteStationFromLineCommand;
import subway.application.dto.LineCreateCommand;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.domain.service.RemoveStationFromLineService;
import subway.exception.DuplicateLineException;
import subway.exception.NotFoundLineException;
import subway.exception.NotFoundStationException;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final RemoveStationFromLineService removeStationFromLineService;

    public LineService(final LineRepository lineRepository,
                       final StationRepository stationRepository,
                       final RemoveStationFromLineService removeStationFromLineService) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.removeStationFromLineService = removeStationFromLineService;
    }

    public Long create(final LineCreateCommand command) {
        if (lineRepository.findByName(command.getLineName()).isPresent()) {
            throw new DuplicateLineException(command.getLineName());
        }
        final Station up = findStationByName(command.getUpTerminalName());
        final Station down = findStationByName(command.getDownTerminalName());
        final Sections sections = new Sections(new Section(up, down, command.getDistance()));
        return lineRepository.save(new Line(command.getLineName(), sections));
    }

    private Station findStationByName(final String name) {
        return stationRepository.findByName(name)
                .orElseThrow(() -> new NotFoundStationException(name));
    }

    public void addStation(final AddStationToLineCommand command) {
        final Line line = findLineByName(command.getLineName());
        final Station up = findStationByName(command.getUpStationName());
        final Station down = findStationByName(command.getDownStationName());
        final Section section = new Section(up, down, command.getDistance());
        line.addSection(section);
        lineRepository.update(line);
    }

    public void removeStation(final DeleteStationFromLineCommand command) {
        final Line line = findLineByName(command.getLineName());
        final Station station = findStationByName(command.getDeleteStationName());
        removeStationFromLineService.remove(lineRepository, line, station);
    }

    private Line findLineByName(final String name) {
        return lineRepository.findByName(name)
                .orElseThrow(() -> new NotFoundLineException(name));
    }
}

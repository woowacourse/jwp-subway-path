package subway.application;

import static subway.exception.line.LineExceptionType.DUPLICATE_LINE_NAME;
import static subway.exception.line.LineExceptionType.NOT_FOUND_LINE;
import static subway.exception.station.StationExceptionType.NOT_FOUND_STATION;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.AddStationToLineCommand;
import subway.application.dto.DeleteStationFromLineCommand;
import subway.application.dto.LineCreateCommand;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.LineValidator;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.domain.service.RemoveStationFromLineService;
import subway.exception.line.LineException;
import subway.exception.station.StationException;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final LineValidator lineValidator;
    private final RemoveStationFromLineService removeStationFromLineService;

    public LineService(final LineRepository lineRepository,
                       final StationRepository stationRepository,
                       final LineValidator lineValidator,
                       final RemoveStationFromLineService removeStationFromLineService) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.lineValidator = lineValidator;
        this.removeStationFromLineService = removeStationFromLineService;
    }

    public Long create(final LineCreateCommand command) {
        validateDuplicateLineName(command);
        final Station up = findStationByName(command.upTerminalName());
        final Station down = findStationByName(command.downTerminalName());
        final Section section = new Section(up, down, command.distance());
        lineValidator.validateSectionConsistency(section);
        return lineRepository.save(new Line(command.lineName(), new Sections(section)));
    }

    private void validateDuplicateLineName(final LineCreateCommand command) {
        if (lineRepository.findByName(command.lineName()).isPresent()) {
            throw new LineException(DUPLICATE_LINE_NAME);
        }
    }

    private Station findStationByName(final String name) {
        return stationRepository.findByName(name)
                .orElseThrow(() -> new StationException(NOT_FOUND_STATION));
    }

    public void addStation(final AddStationToLineCommand command) {
        final Line line = findLineByName(command.lineName());
        final Station up = findStationByName(command.upStationName());
        final Station down = findStationByName(command.downStationName());
        final Section section = new Section(up, down, command.distance());
        lineValidator.validateSectionConsistency(section);
        line.addSection(section);
        lineRepository.update(line);
    }

    public void removeStation(final DeleteStationFromLineCommand command) {
        final Line line = findLineByName(command.lineName());
        final Station station = findStationByName(command.deleteStationName());
        removeStationFromLineService.remove(line, station);
    }

    private Line findLineByName(final String name) {
        return lineRepository.findByName(name)
                .orElseThrow(() -> new LineException(NOT_FOUND_LINE));
    }
}

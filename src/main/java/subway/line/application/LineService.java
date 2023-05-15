package subway.line.application;

import static subway.line.exception.line.LineExceptionType.NOT_FOUND_LINE;
import static subway.line.exception.station.StationExceptionType.NOT_FOUND_STATION;

import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.dto.AddStationToLineCommand;
import subway.line.application.dto.DeleteStationFromLineCommand;
import subway.line.application.dto.LineCreateCommand;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.domain.LineValidator;
import subway.line.domain.Section;
import subway.line.domain.Station;
import subway.line.domain.StationRepository;
import subway.line.domain.event.ChangeLineEvent;
import subway.line.domain.service.RemoveStationFromLineService;
import subway.line.exception.line.LineException;
import subway.line.exception.station.StationException;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final LineValidator lineValidator;
    private final RemoveStationFromLineService removeStationFromLineService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public LineService(final LineRepository lineRepository,
                       final StationRepository stationRepository,
                       final LineValidator lineValidator,
                       final RemoveStationFromLineService removeStationFromLineService,
                       final ApplicationEventPublisher applicationEventPublisher) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.lineValidator = lineValidator;
        this.removeStationFromLineService = removeStationFromLineService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public UUID create(final LineCreateCommand command) {
        lineValidator.validateDuplicateLineName(command.lineName());
        final Line line = new Line(command.lineName(), command.surcharge());
        lineRepository.save(line);
        applicationEventPublisher.publishEvent(new ChangeLineEvent());
        return line.id();
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
        applicationEventPublisher.publishEvent(new ChangeLineEvent());
    }

    public void removeStation(final DeleteStationFromLineCommand command) {
        final Line line = findLineByName(command.lineName());
        final Station station = findStationByName(command.deleteStationName());
        removeStationFromLineService.remove(line, station);
        applicationEventPublisher.publishEvent(new ChangeLineEvent());
    }

    private Line findLineByName(final String name) {
        return lineRepository.findByName(name)
                .orElseThrow(() -> new LineException(NOT_FOUND_LINE));
    }
}

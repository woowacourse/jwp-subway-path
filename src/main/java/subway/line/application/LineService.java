package subway.line.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.dto.LineCreationDto;
import subway.line.application.dto.StationAdditionToLineDto;
import subway.line.application.dto.StationDeletionFromLineDto;
import subway.line.domain.DummyTerminalStation;
import subway.line.domain.Line;
import subway.line.domain.Lines;
import subway.line.domain.MiddleSection;
import subway.line.exception.LineNotFoundException;
import subway.line.repository.LineRepository;
import subway.line.ui.dto.LineDto;
import subway.line.ui.dto.SectionDto;
import subway.station.application.StationService;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineService {

    private final StationService stationService;
    private final LineRepository lineRepository;

    @Autowired
    public LineService(LineRepository lineRepository, StationService stationService) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public long addStationToLine(StationAdditionToLineDto stationAdditionToLineDto) {
        final long stationId = stationService.createStationIfNotExist(stationAdditionToLineDto.getStationName());
        final Station stationToAdd = new Station(stationAdditionToLineDto.getStationName());

        final Line line = findLineOrThrow(stationAdditionToLineDto.getLineId());
        final Station upstream = findExistingStationByName(stationAdditionToLineDto.getUpstreamName());
        final Station downstream = findExistingStationByName(stationAdditionToLineDto.getDownstreamName());

        line.addStation(stationToAdd, upstream, downstream, stationAdditionToLineDto.getDistanceToUpstream());
        lineRepository.updateLine(line);

        return stationId;
    }

    private Station findExistingStationByName(String stationName) {
        if (stationName.equals(DummyTerminalStation.STATION_NAME)) {
            return DummyTerminalStation.getInstance();
        }
        return stationService.findStationByName(stationName);
    }

    public void deleteStationFromLine(StationDeletionFromLineDto stationDeletionFromLineDto) {
        Station stationToDelete = stationService.findStationById(stationDeletionFromLineDto.getStationId());

        final Line line = findLineOrThrow(stationDeletionFromLineDto.getLineId());
        line.deleteStation(stationToDelete);
        if (line.isLineEmpty()) {
            lineRepository.deleteLine(line);
        } else {
            lineRepository.updateLine(line);
        }
    }

    public long createLine(LineCreationDto lineCreationDto) {
        stationService.createStationIfNotExist(lineCreationDto.getUpstreamName());
        stationService.createStationIfNotExist(lineCreationDto.getDownstreamName());
        Station upstream = new Station(lineCreationDto.getUpstreamName());
        Station downstream = new Station(lineCreationDto.getDownstreamName());

        MiddleSection section = new MiddleSection(upstream, downstream, lineCreationDto.getDistance());
        Line lineToAdd = new Line(lineCreationDto.getLineName(), List.of(section));

        final Lines allLines = lineRepository.findAllLines();
        allLines.addLine(lineToAdd);

        return lineRepository.createLine(lineToAdd);
    }

    @Transactional(readOnly = true)
    public LineDto findLineById(long id) {
        return toLineDto(findLineOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<LineDto> findAllLines() {
        return lineRepository.findAllLines()
                             .getLines()
                             .stream()
                             .map(this::toLineDto)
                             .collect(Collectors.toUnmodifiableList());
    }

    private Line findLineOrThrow(long stationRemovalFromLineDto) {
        return lineRepository.findLineById(stationRemovalFromLineDto)
                             .orElseThrow(() -> new LineNotFoundException("존재하지 않는 노선입니다"));
    }

    private LineDto toLineDto(Line line) {
        return new LineDto(toSectionDtos(line.getSections()), line.getName());
    }

    private List<SectionDto> toSectionDtos(List<MiddleSection> sections) {
        return sections.stream()
                       .map(section -> new SectionDto(
                               section.getUpstreamName(),
                               section.getDownstreamName(),
                               section.getDistance())
                       )
                       .collect(Collectors.toUnmodifiableList());
    }
}

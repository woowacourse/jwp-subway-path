package subway.line.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.dto.LineCreateDto;
import subway.line.dto.LineResponseDto;
import subway.line.dto.ShortestPathRequest;
import subway.line.dto.ShortestPathResponse;
import subway.section.domain.Section;
import subway.section.dto.SectionCreateDto;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;
import subway.station.dto.StationResponseDto;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public Long createLine(final LineCreateDto lineCreateDto) {
        final Line line = lineRepository.createLine(lineCreateDto.getName());
        return line.getId();
    }

    public Long addSection(final SectionCreateDto sectionCreateDto) {
        final Line line = lineRepository.findById(sectionCreateDto.getLineId());
        final Station upStation = stationRepository.findById(sectionCreateDto.getUpStationId());
        final Station downStation = stationRepository.findById(sectionCreateDto.getDownStationId());
        final Section section = Section.of(upStation, downStation, sectionCreateDto.getDistance());

        line.add(section);
        lineRepository.updateLine(line);
        return line.getId();
    }

    @Transactional(readOnly = true)
    public List<StationResponseDto> findSortedStations(final Long lineId) {
        final Line line = lineRepository.findById(lineId);

        return mapToStationResponseDtos(line.getSections());
    }

    private List<StationResponseDto> mapToStationResponseDtos(final List<Section> sortedSections) {
        final List<StationResponseDto> stationResponseDtos = new ArrayList<>();

        for (Section sortedSection : sortedSections) {
            final Station upStation = sortedSection.getUpStation();
            stationResponseDtos.add(new StationResponseDto(upStation.getId(), upStation.getName()));
        }

        final Station lastStation = sortedSections.get(sortedSections.size() - 1).getDownStation();
        stationResponseDtos.add(new StationResponseDto(lastStation.getId(), lastStation.getName()));

        return stationResponseDtos;
    }

    @Transactional(readOnly = true)
    public List<LineResponseDto> findAllLines() {
        final List<Line> lines = lineRepository.findAll();

        return lines.stream()
            .map((line) -> new LineResponseDto(line.getId(), line.getLineName(),
                findSortedStations(line.getId())))
            .collect(Collectors.toList());
    }

    public void removeStationBy(final Long lineId, final Long stationId) {
        final Line line = lineRepository.findById(lineId);
        final Station station = stationRepository.findById(stationId);

        line.removeStation(station);
        lineRepository.updateLine(line);
    }
}

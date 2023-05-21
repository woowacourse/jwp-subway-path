package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.LineDto;
import subway.application.dto.StationDto;
import subway.domain.*;
import subway.domain.repository.LineRepository;
import subway.domain.repository.SectionRepository;
import subway.domain.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository,
                       StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public Long saveLine(String lineName) {
        Line line = new Line(lineName);

        return lineRepository.save(line);
    }

    public List<LineDto> findAllLinesDetails() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::toLineDetailsDto)
                .collect(Collectors.toList());
    }

    private LineDto toLineDetailsDto(Line line) {
        Long lineId = lineRepository.findIdByName(line.getName());
        Sections sections = sectionRepository.findAllByLineId(lineId);
        if (sections.isEmpty()) {
            return new LineDto(lineId, line.getName(), new ArrayList<>());
        }
        List<Station> orderedStations = sections.getOrderedStations();

        List<StationDto> stations = orderedStations.stream()
                .map(this::toStationDto)
                .collect(Collectors.toList());

        return new LineDto(lineId, line.getName(), stations);
    }

    private StationDto toStationDto(Station station) {
        Long stationId = stationRepository.findIdByName(station.getName());

        return new StationDto(stationId, station.getName());
    }

    public LineDto findLineById(Long id) {
        Line line = lineRepository.findLineById(id);

        return toLineDetailsDto(line);
    }
}

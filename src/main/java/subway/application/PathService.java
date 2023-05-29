package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.*;
import subway.domain.graph.JGraphTSubwayGraph;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.dto.SectionResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(final StationRepository stationRepository, final LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse calculatePath(final PathRequest pathRequest) {
        Station from = stationRepository.findById(pathRequest.getFromStationId());
        Station to = stationRepository.findById(pathRequest.getToStationId());
        List<Line> allLines = lineRepository.findAll();
        Lines lines = new Lines(allLines);

        SubwayMap subwayMap = new SubwayMap(lines, new JGraphTSubwayGraph());
        List<Section> sections = subwayMap.calculateShortestPaths(from, to);
        Integer distance = subwayMap.calculateTotalDistance(sections);
        Integer charge = SubwayFare.decideFare(distance).calculateCharge(distance);

        List<SectionResponse> sectionResponses = convertSectionResponses(lines, sections);
        return new PathResponse(distance, charge, sectionResponses);
    }

    private List<SectionResponse> convertSectionResponses(final Lines lines, final List<Section> sections) {
        return sections.stream()
                .map(section -> SectionResponse.of(section, lines.getLineOfSection(section)))
                .collect(Collectors.toList());
    }
}

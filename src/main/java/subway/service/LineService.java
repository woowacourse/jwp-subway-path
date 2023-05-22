package subway.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionCreateRequest;
import subway.dto.StationResponse;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository,
                       final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public Long save(final LineRequest lineRequest) {
        Line newLine = Line.createWithoutId(lineRequest.getName(), Collections.emptyList());
        return lineRepository.save(newLine);
    }

    @Transactional(readOnly = true)
    public LineResponse findById(final Long id) {
        Line line = lineRepository.findById(id);
        List<StationResponse> stationsResponse = line.getStations().stream()
                .map(station -> new StationResponse(id, station.getName()))
                .collect(Collectors.toList());

        return new LineResponse(id, line.getName(), stationsResponse);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        Lines lines = lineRepository.findAll();

        return lines.getLines().stream()
                .map(line -> {
                    List<StationResponse> stationResponses = line.getStations().stream()
                            .map(station -> new StationResponse(station.getId(), station.getName()))
                            .collect(Collectors.toList());
                    return new LineResponse(line.getId(), line.getName(), stationResponses);
                })
                .collect(Collectors.toList());
    }

    public Long createSectionInLine(final Long lineId, final SectionCreateRequest sectionCreateRequest) {
        Station upStation = stationRepository.findById(sectionCreateRequest.getUpStationId());
        Station downStation = stationRepository.findById(sectionCreateRequest.getDownStationId());

        Line line = lineRepository.findById(lineId);
        List<Section> originalSections = line.getSectionsByList();

        line.addSection(upStation, downStation, sectionCreateRequest.getDistance());

        Long newSectionId = saveNewSections(lineId, originalSections, line);
        deleteOriginalSection(lineId, originalSections, line);

        return newSectionId;
    }

    private Long saveNewSections(final long lineId, final List<Section> originalSections, final Line line) {
        List<Section> newSections = line.getSectionsByList();
        newSections.removeAll(originalSections);

        Section newSection = newSections.get(0);

        return sectionRepository.save(lineId, newSection);
    }

    private void deleteOriginalSection(final long lineId, final List<Section> originalSections, final Line line) {
        List<Section> newSections = line.getSectionsByList();
        originalSections.removeAll(newSections);

        if (originalSections.isEmpty()) {
            return;
        }

        Section deletedSection = originalSections.get(0);

        sectionRepository.delete(lineId, deletedSection);
    }

    public void deleteStation(final Long lineId, final Long stationId) {
        Station station = stationRepository.findById(stationId);
        Line line = lineRepository.findById(lineId);
        List<Section> originalSections = line.getSectionsByList();

        line.deleteStation(station);

        if (line.isEmptyLine()) {
            sectionRepository.deleteByLineId(lineId);
            return;
        }

        saveNewSections(lineId, originalSections, line);
        deleteOriginalSection(lineId, originalSections, line);
    }
}

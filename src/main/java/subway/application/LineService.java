package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.request.LineRequest;
import subway.application.request.SectionRequest;
import subway.application.request.StationRequest;
import subway.application.response.LineResponse;
import subway.domain.line.Line;
import subway.domain.line.LineName;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.persistence.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public long saveLine(final LineRequest request) {
        final LineName lineName = new LineName(request.getName());
        return lineRepository.insert(lineName);
    }

    public List<LineResponse> findLineResponses() {
        final List<Line> lines = findLines();
        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineRepository.findLines();
    }

    public LineResponse findLineResponseById(final Long id) {
        final Line line = findById(id);
        return LineResponse.from(line);
    }

    @Transactional
    public void registerStation(final Long lineId, final SectionRequest request) {
        final Line line = findById(lineId);

        final Section newSection = createSection(request);
        final Line addedLine = line.addSection(newSection);

        final Sections deleteSections = line.getSections().getDifferenceOfSet(addedLine.getSections());
        final Sections insertSections = addedLine.getSections().getDifferenceOfSet(line.getSections());

        lineRepository.deleteSections(deleteSections);
        lineRepository.insertSections(lineId, insertSections);
    }

    @Transactional
    public void unregisterStation(final Long id, final StationRequest request) {
        final Line line = findById(id);

        final Station station = lineRepository.findStationByName(request.getName());
        final Line deletedLine = line.removeStation(station);

        final Sections deleteSections = line.getSections().getDifferenceOfSet(deletedLine.getSections());
        final Sections insertSections = deletedLine.getSections().getDifferenceOfSet(line.getSections());

        lineRepository.deleteSections(deleteSections);
        lineRepository.insertSections(line.getId(), insertSections);
    }

    public Line findById(final Long id) {
        return lineRepository.findById(id);
    }

    private Section createSection(final SectionRequest request) {
        final Station beforeStation = lineRepository.findStationByName(request.getBeforeStationName());
        final Station nextStation = lineRepository.findStationByName(request.getNextStationName());
        return new Section(beforeStation, nextStation, new Distance(request.getDistance()));
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {

        lineRepository.updateName(id, lineUpdateRequest);
    }

    public void deleteLineById(final Long id) {
        lineRepository.deleteLineById(id);
    }
}

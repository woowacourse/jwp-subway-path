package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.dto.request.LineRequest;
import subway.dto.request.SectionRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.SectionResponse;
import subway.dto.response.StationResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Long saveLine(final LineRequest request) {
        final Line line = new Line(
                new LineName(request.getName()),
                new LineColor(request.getColor())
        );

        return lineRepository.save(line);
    }

    @Transactional
    public void insertSectionToLine(final Long lineId, final SectionRequest request) {
        final Line line = lineRepository.findById(lineId);
        final Station upStation = stationRepository.findById(request.getUpStationId());
        final Station downStation = stationRepository.findById(request.getDownStationId());
        final Distance distance = new Distance(request.getDistance());

        final Section section = new Section(upStation, downStation, distance);
        final Line updateSections = line.addSection(section);

        lineRepository.updateSections(updateSections);
    }

    @Transactional
    public void deleteStationFromLine(final Long lineId, final Long stationId) {
        final Line line = lineRepository.findById(lineId);
        final Station station = stationRepository.findById(stationId);

        final Line updateSection = line.deleteStation(station);

        lineRepository.updateSections(updateSection);
    }

    public LineResponse findLineById(final Long id) {
        final Line line = lineRepository.findById(id);
        final List<SectionResponse> sectionResponses = createSectionResponses(line);

        return new LineResponse(
                line.getId(),
                line.getLineName().name(),
                line.getLineColor().color(),
                sectionResponses
        );
    }

    private List<SectionResponse> createSectionResponses(final Line line) {
        return line.getSections().sections().stream()
                .map(this::createSectionResponse).collect(Collectors.toUnmodifiableList());
    }

    private SectionResponse createSectionResponse(final Section section) {
        return new SectionResponse(
                section.getId(),
                createStationResponse(section.getUpStation()),
                createStationResponse(section.getDownStation()),
                section.getDistance().distance()
        );
    }

    private StationResponse createStationResponse(final Station station) {
        return new StationResponse(station.getId(), station.getName().name());
    }

    public List<LineResponse> findAllLine() {
        final List<Line> lines = lineRepository.findAllLine();

        return lines.stream().map(
                line -> new LineResponse(
                        line.getId(),
                        line.getLineName().name(),
                        line.getLineColor().color(),
                        createSectionResponses(line)
                )
        ).collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void updateLineById(Long id, LineRequest request) {
        lineRepository.update(
                new Line(id, new LineName(request.getName()), new LineColor(request.getColor()))
        );
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.delete(id);
    }
}

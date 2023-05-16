package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    public Long saveLine(LineRequest request) {
        final Line line = new Line(request.getName(), request.getColor());
        final Long lineId = lineRepository.insert(line);

        final Section section = new Section(
                request.getDistance(),
                new Station(request.getUpStationId()),
                new Station(request.getDownStationId()),
                lineId
        );
        sectionRepository.insert(section);

        return lineId;
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();

        return persistLines.stream()
                .map(line -> findLineResponseById(line.getId()))
                .collect(toList());
    }

    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        final Sections sections = sectionRepository.findAllByLineId(id);

        final List<Station> stations = sections.findAllStationsByOrder();

        Line persistLine = lineRepository.findById(id);
        return LineResponse.of(persistLine, stations);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}

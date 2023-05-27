package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineStationResponse;
import subway.dto.SectionResponse;
import subway.dto.StationResponse;
import subway.repository.LineRepository;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Long save(final LineRequest request) {
        final Line line = new Line(request.getName(), request.getColor());
        return lineRepository.save(line);
    }

    public List<LineStationResponse> findAll() {
        final List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(it -> LineStationResponse.from(LineResponse.of(it), getStationResponses(it),
                        getSectionResponses(it)))
                .collect(Collectors.toList());
    }

    public LineStationResponse findById(final Long id) {
        final Line line = lineRepository.findById(id);
        final List<StationResponse> stationResponses = getStationResponses(line);
        final List<SectionResponse> sectionResponses = getSectionResponses(line);
        return LineStationResponse.from(LineResponse.of(line), stationResponses, sectionResponses);
    }

    private List<StationResponse> getStationResponses(final Line line) {
        return line.sortStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    private List<SectionResponse> getSectionResponses(final Line line) {
        return line.getSections().stream()
                .map(SectionResponse::of)
                .collect(Collectors.toList());
    }

    public void update(final Long id, final LineRequest lineUpdateRequest) {
        final Line line = new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        lineRepository.update(id, line);
    }

    public void deleteById(final Long id) {
        lineRepository.deleteById(id);
    }
}

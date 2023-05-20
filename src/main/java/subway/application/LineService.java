package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import subway.dao.LineRepository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse create(final LineRequest request) {
        final Line line = lineRepository.insert(request.getName(), request.getColor(), request.getAdditionalFee());
        return LineResponse.of(line);
    }

    public List<LineResponse> findAll() {
        return lineRepository.findAll().stream()
            .flatMap(line -> Stream.of(findById(line.getId())))
            .collect(Collectors.toList());
    }

    public LineResponse findById(final Long id) {
        final Line line = lineRepository.findById(id);
        final List<Section> sectionsInLine = lineRepository.findSectionsByLineId(id);
        final List<StationResponse> stationResponses = Sections.of(sectionsInLine).getSortedStations()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());

        return LineResponse.withStationResponses(line, stationResponses);
    }

    public void update(final Long id, final LineRequest lineUpdateRequest) {
        lineRepository.update(id, lineUpdateRequest);
    }

    public void deleteById(final Long id) {
        lineRepository.deleteById(id);
    }
}

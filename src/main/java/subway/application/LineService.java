package subway.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.dto.line.LineCreateRequest;
import subway.dto.line.LineDetailResponse;
import subway.dto.line.LineResponse;
import subway.dto.line.LineUpdateRequest;
import subway.dto.section.SectionResponse;
import subway.exception.IllegalLineException;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineCreateRequest request) {
        Line line = new Line(request.getLineName(), request.getColor());
        if (lineRepository.isDuplicateLine(line)) {
            throw new IllegalLineException("해당 노선의 색, 또는 이름이 중복됩니다.");
        }
        long savedId = lineRepository.save(line);
        return LineResponse.from(lineRepository.findById(savedId));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLineResponses() {
        List<Line> lines = lineRepository.findAllWithNoSections();
        return lines.stream()
                .map(LineResponse::from)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<LineDetailResponse> findDetailLineResponses() {
        return lineRepository.findAll()
                .stream()
                .map(line -> new LineDetailResponse(LineResponse.from(line), mapToSectionResponses(line.getSections())))
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public LineDetailResponse findDetailLineResponse(Long id) {
        Line line = lineRepository.findById(id);
        return new LineDetailResponse(LineResponse.from(line), mapToSectionResponses(line.getSections()));
    }

    private List<SectionResponse> mapToSectionResponses(List<Section> sections) {
        return sections.stream()
                .map(SectionResponse::from)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        Line line = lineRepository.findByIdWithNoSections(id);
        return LineResponse.from(line);
    }

    public LineResponse updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        return LineResponse.from(
                lineRepository.update(new Line(id, lineUpdateRequest.getLineName(), lineUpdateRequest.getColor())));
    }

    public void deleteLineById(Long id) {
        Line line = lineRepository.findByIdWithNoSections(id);
        lineRepository.delete(line);
    }
}

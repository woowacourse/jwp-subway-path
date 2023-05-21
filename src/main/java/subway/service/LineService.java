package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(final LineRequest request) {
        Line newLine = Line.of(request.getName(), request.getColor(), request.getExtraFare());
        validateDuplicatedName(newLine);
        validateDuplicatedColor(newLine);
        return LineResponse.from(lineRepository.insert(newLine));
    }

    private void validateDuplicatedName(final Line oldLine) {
        if (lineRepository.countByName(oldLine.getName()) != 0) {
            throw new IllegalArgumentException("[ERROR] 중복된 노선 이름을 등록할 수 없습니다.");
        }
    }

    private void validateDuplicatedColor(final Line newLine) {
        if (lineRepository.countByColor(newLine.getColor()) != 0) {
            throw new IllegalArgumentException("[ERROR] 중복된 노선 색상을 등록할 수 없습니다.");
        }
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineResponse findLineById(final long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 노선입니다."));
        return LineResponse.from(line);
    }

    public void updateLine(final long id, final LineRequest request) {
        Line oldLine = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 노선입니다."));
        Line updatedLine = Line.of(id, request.getName(), request.getColor(), oldLine.getSections(), oldLine.getExtraFare());
        if (!oldLine.isSameName(updatedLine)) {
            validateDuplicatedName(updatedLine);
        }
        if (!oldLine.isSameColor(updatedLine)) {
            validateDuplicatedColor(updatedLine);
        }
        lineRepository.update(updatedLine);
    }

    public void deleteLineById(final long id) {
        lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 노선입니다."));
        lineRepository.deleteById(id);
    }

}

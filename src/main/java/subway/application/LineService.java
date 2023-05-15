package subway.application;

import static java.util.Collections.EMPTY_LIST;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.exception.LineDuplicatedException;
import subway.exception.NotFoundException;
import subway.repository.LineRepository;
import subway.ui.dto.LineRequest;

@Service
public class LineService {

    LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public long createLine(LineRequest request) {
        validateDuplicatedLine(request);
        return lineRepository.saveNewLine(new Line(null, request.getName(), request.getColor(), EMPTY_LIST));
    }

    private void validateDuplicatedLine(LineRequest lineRequest) {
        lineRepository.findIdByName(lineRequest.getName())
                .ifPresent(id -> {
                    throw new LineDuplicatedException("이미 존재하는 노선 이름입니다.");
                });
        lineRepository.findIdByColor(lineRequest.getColor())
                .ifPresent(id -> {
                    throw new LineDuplicatedException("이미 존재하는 노선 색입니다.");
                });
    }

    public Line findLineById(Long lineId) {
        return lineRepository.findLineById(lineId)
                .orElseThrow(() -> new NotFoundException("해당 노선이 존재하지 않습니다."));
    }

    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public void updateLineInfo(Long lineId, LineRequest request) {
        validateDuplicatedLine(request);
        Line line = findLineById(lineId);
        Line lineToUpdate = new Line(lineId, request.getName(), request.getColor(), line.getSections());
        lineRepository.updateLineInfo(lineToUpdate);
    }

    public void deleteLineById(Long lineId) {
        Line line = findLineById(lineId);
        lineRepository.delete(line.getId());
    }

}

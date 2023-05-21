package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.exception.OptionalHasNoLineException;
import subway.domain.line.Line;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.persistence.dao.LineDao;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineDao.insert(Line.of(request.getName(), request.getColor(), request.getExtraFare()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id)
                .orElseThrow(OptionalHasNoLineException::new);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(Line.of(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor(), lineUpdateRequest.getExtraFare()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}

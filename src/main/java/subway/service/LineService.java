package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.H2LineDao;
import subway.domain.Line;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final H2LineDao lineDao;

    public LineService(H2LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineDao.insert(Line.of(request.getName(), request.getColor()));
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
        return lineDao.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(Line.of(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}

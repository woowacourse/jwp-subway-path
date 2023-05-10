package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.domain.LineName;
import subway.dto.LineNodeRequests;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineDao lineDao;

    public LineService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = lineDao.insert(new LineName(request.getName()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        final List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(final Long id) {
        final Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public void registerLine(final LineNodeRequests requests) {

    }

    public Line findLineById(final Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.updateName(id, new LineName(lineUpdateRequest.getName()));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }

}

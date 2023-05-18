package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

@Service
public class LineService {
    
    private final LineDao lineDao;
    
    public LineService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }
    
    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = this.lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }
    
    public List<LineResponse> findLineResponses() {
        final List<Line> persistLines = this.findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }
    
    public List<Line> findLines() {
        return this.lineDao.findAll();
    }
    
    public LineResponse findLineResponseById(final Long id) {
        final Line persistLine = this.findLineById(id);
        return LineResponse.of(persistLine);
    }
    
    public Line findLineById(final Long id) {
        return this.lineDao.findById(id);
    }
    
    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        this.lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }
    
    public void deleteLineById(final Long id) {
        this.lineDao.deleteById(id);
    }
    
}

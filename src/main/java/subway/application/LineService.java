package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    
    @Transactional
    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = this.lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }
    
    @Transactional(readOnly = true)
    public List<LineResponse> findLineResponses() {
        final List<Line> persistLines = this.findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<Line> findLines() {
        return this.lineDao.findAll();
    }
    
    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(final Long id) {
        final Line persistLine = this.findLineById(id);
        return LineResponse.of(persistLine);
    }
    
    @Transactional(readOnly = true)
    public Line findLineById(final Long id) {
        return this.lineDao.findById(id);
    }
    
    @Transactional
    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        this.lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }
    
    @Transactional
    public void deleteLineById(final Long id) {
        this.lineDao.deleteById(id);
    }
    
}

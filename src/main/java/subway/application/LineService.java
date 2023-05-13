package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LinePropertyDao;
import subway.domain.LineProperty;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final LinePropertyDao linePropertyDao;

    public LineService(LinePropertyDao linePropertyDao) {
        this.linePropertyDao = linePropertyDao;
    }

    public LineResponse saveLine(LineRequest request) {
        LineProperty lineProperty = linePropertyDao.insert(
                new LineProperty(null, request.getName(), request.getColor()));
        return LineResponse.of(lineProperty);
    }

    public List<LineResponse> findLineResponses() {
        List<LineProperty> allLineProperties = findLines();
        return allLineProperties.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<LineProperty> findLines() {
        return linePropertyDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        LineProperty lineProperty = linePropertyDao.findById(id);
        return LineResponse.of(lineProperty);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        linePropertyDao.update(new LineProperty(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        linePropertyDao.deleteById(id);
    }
}

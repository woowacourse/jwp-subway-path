package subway.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.entity.LineEntity;
import subway.dto.line.LineCreateRequest;
import subway.dto.line.LineResponse;
import subway.dto.line.LineUpdateRequest;
import subway.exception.LineNotFoundException;

@Service
@Transactional
public class LineService {
    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public long saveLine(LineCreateRequest request) {
        return lineDao.insert(new LineEntity(request.getLineName(), request.getColor()));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLineResponses() {
        List<LineEntity> lines = lineDao.findAll();
        return lines.stream()
                .map(LineResponse::from)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(Long id) {
        Optional<LineEntity> lineEntity = lineDao.findById(id);
        if (lineEntity.isPresent()) {
            LineEntity line = lineEntity.get();
            return new LineResponse(line.getId(), line.getName(), line.getColor());
        }
        throw new LineNotFoundException();
    }

    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        lineDao.update(new LineEntity(id, lineUpdateRequest.getLineName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }
}

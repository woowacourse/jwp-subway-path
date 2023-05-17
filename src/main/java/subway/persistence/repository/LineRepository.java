package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.persistence.entity.LineEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LineRepository {
    private final LineDao lineDao;

    public LineRepository(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public LineResponse saveLine(LineRequest request) {
        LineEntity persistLine = lineDao.insert(new LineEntity(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        List<LineEntity> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    private List<LineEntity> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        LineEntity persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    private LineEntity findLineById(Long id) {
        Optional<LineEntity> lineEntity = lineDao.findById(id);
        if (lineEntity.isPresent()) {
            return lineEntity.get();
        }
        throw new IllegalArgumentException("존재하지 않는 노선입니다");
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new LineEntity(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }
}

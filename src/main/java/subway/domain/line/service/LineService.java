package subway.domain.line.service;

import org.springframework.stereotype.Service;
import subway.domain.line.dao.LineDao;
import subway.domain.line.dto.LineRequest;
import subway.domain.line.entity.LineEntity;

import java.util.List;
import java.util.Optional;

@Service
public class LineService {

    private final LineDao lineDao;

    public LineService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public List<LineEntity> findAllLine() {
        Optional<List<LineEntity>> findLines = lineDao.findAll();
        if (findLines.isEmpty()) {
            throw new IllegalArgumentException("해당 노선이 존재하지 않습니다.");
        }
        return findLines.get();
    }

    public LineEntity findLineById(Long id) {
        Optional<LineEntity> findLine = lineDao.findById(id);
        if (findLine.isEmpty()) {
            throw new IllegalArgumentException("해당 노선이 존재하지 않습니다.");
        }
        return findLine.get();
    }

    public LineEntity saveLine(final LineRequest request) {
        Optional<LineEntity> line = lineDao.findByName(request.getName());
        if (line.isPresent()) {
            throw new IllegalArgumentException("노선 이름이 이미 존재합니다. 유일한 노선 이름을 사용해주세요.");
        }
        return lineDao.insert(new LineEntity(request.getName(), request.getColor()));
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        Optional<LineEntity> findLine = lineDao.findById(id);
        if (findLine.isEmpty()) {
            throw new IllegalArgumentException("해당 노선이 존재하지 않습니다.");
        }
        lineDao.update(new LineEntity(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        Optional<LineEntity> findLine = lineDao.findById(id);
        if (findLine.isEmpty()) {
            throw new IllegalArgumentException("해당 노선이 존재하지 않습니다.");
        }
        lineDao.deleteById(id);
    }
}

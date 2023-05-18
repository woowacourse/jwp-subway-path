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
        return lineDao.findAll();
    }

    public LineEntity findLineById(Long id) {
        return lineDao.findById(id);
    }

    public LineEntity saveLine(final LineRequest request) {
        Optional<LineEntity> line = lineDao.findByName(request.getName());
        if (line.isPresent()) {
            throw new IllegalArgumentException("노선 이름이 이미 존재합니다. 유일한 노선 이름을 사용해주세요.");
        }
        return lineDao.insert(new LineEntity(request.getName(), request.getColor()));
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.update(new LineEntity(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }
}

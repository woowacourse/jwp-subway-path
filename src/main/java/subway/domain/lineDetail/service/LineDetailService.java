package subway.domain.lineDetail.service;

import org.springframework.stereotype.Service;
import subway.domain.lineDetail.dao.LineDetailDao;
import subway.domain.lineDetail.dto.LineDetailRequest;
import subway.domain.lineDetail.entity.LineDetailEntity;

import java.util.List;
import java.util.Optional;

@Service
public class LineDetailService {

    private final LineDetailDao lineDetailDao;

    public LineDetailService(final LineDetailDao lineDetailDao) {
        this.lineDetailDao = lineDetailDao;
    }

    public List<LineDetailEntity> findAllLine() {
        return lineDetailDao.findAll();
    }

    public LineDetailEntity findLineById(Long id) {
        return lineDetailDao.findById(id);
    }

    public LineDetailEntity saveLine(final LineDetailRequest request) {
        Optional<LineDetailEntity> line = lineDetailDao.findByName(request.getName());
        if (line.isPresent()) {
            throw new IllegalArgumentException("노선 이름이 이미 존재합니다. 유일한 노선 이름을 사용해주세요.");
        }
        return lineDetailDao.insert(new LineDetailEntity(request.getName(), request.getColor()));
    }

    public void updateLine(final Long id, final LineDetailRequest lineUpdateRequest) {
        lineDetailDao.update(new LineDetailEntity(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineDetailDao.deleteById(id);
    }
}

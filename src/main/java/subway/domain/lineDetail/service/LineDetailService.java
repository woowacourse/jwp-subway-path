package subway.domain.lineDetail.service;

import org.springframework.stereotype.Service;
import subway.domain.lineDetail.dao.LineDetailDao;
import subway.domain.lineDetail.domain.LineDetail;
import subway.domain.lineDetail.dto.LineDetailRequest;
import subway.domain.lineDetail.dto.LineDetailResponse;

import java.util.List;
import java.util.Optional;

@Service
public class LineDetailService {

    private final LineDetailDao lineDetailDao;

    public LineDetailService(final LineDetailDao lineDetailDao) {
        this.lineDetailDao = lineDetailDao;
    }

    public List<LineDetail> findAllLine() {
        return lineDetailDao.findAll();
    }

    public LineDetail findLineById(Long id) {
        return lineDetailDao.findById(id);
    }

    public LineDetailResponse saveLine(final LineDetailRequest request) {
        Optional<LineDetail> line = lineDetailDao.findByName(request.getName());
        if (line.isPresent()) {
            throw new IllegalArgumentException("노선 이름이 이미 존재합니다. 유일한 노선 이름을 사용해주세요.");
        }
        final LineDetail findLineDetail = lineDetailDao.insert(new LineDetail(request.getName(), request.getColor()));
        return LineDetailResponse.of(findLineDetail);
    }

    public void updateLine(final Long id, final LineDetailRequest lineUpdateRequest) {
        lineDetailDao.update(new LineDetail(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineDetailDao.deleteById(id);
    }
}

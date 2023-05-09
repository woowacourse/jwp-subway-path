package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.entity.LineEntity;

@Service
public class LineService {

    private final LineDao lineDao;

    public LineService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public LineResponse saveLine(final LineRequest request) {
        final LineEntity persistLineEntity = lineDao.insert(
            new LineEntity(request.getName(), request.getColor()));
        return LineResponse.of(persistLineEntity);
    }

    public List<LineResponse> findLineResponses() {
        final List<LineEntity> persistLineEntities = findLines();
        return persistLineEntities.stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    public List<LineEntity> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(final Long id) {
        final LineEntity persistLineEntity = findLineById(id);
        return LineResponse.of(persistLineEntity);
    }

    public LineEntity findLineById(final Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.update(
            new LineEntity(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }

}

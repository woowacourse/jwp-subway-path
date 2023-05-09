package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.entity.LineEntity;
import subway.dto.LineRequest;

@Service
public class LineService {
    private final LineDao lineDao;

    public LineService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public Long saveLine(final LineRequest lineRequest) {
        return lineDao.save(new LineEntity(lineRequest.getName()));
    }
}

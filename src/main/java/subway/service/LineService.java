package subway.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.entity.LineEntity;
import subway.dto.LineRequest;
import subway.exception.DuplicateException;
import subway.exception.ErrorCode;

@Service
@Transactional
public class LineService {
    private final LineDao lineDao;

    public LineService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public Long saveLine(final LineRequest lineRequest) {
        if (existLine(lineRequest)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NAME);
        }
        return lineDao.save(new LineEntity(lineRequest.getName()));
    }

    @Transactional(readOnly = true)
    public boolean existLine(final LineRequest lineRequest) {
        try {
            lineDao.findByName(lineRequest.getName());
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}

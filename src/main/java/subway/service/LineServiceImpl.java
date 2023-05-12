package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.entity.LineEntity;

@Service
public class LineServiceImpl implements LineService {

    private final LineDao lineDao;

    public LineServiceImpl(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public LineResponse createLine(final LineRequest lineRequest) {
        final LineEntity line = new LineEntity(lineRequest.getName(), lineRequest.getColor());
        final LineEntity savedLine = lineDao.save(line);
        return LineResponse.of(savedLine);
    }
}

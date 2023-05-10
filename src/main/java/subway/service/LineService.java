package subway.service;

import org.springframework.stereotype.Service;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.LineStationsResponse;
import subway.controller.dto.LinesResponse;
import subway.controller.dto.SectionCreateRequest;
import subway.dao.LineDao;
import subway.domain.line.Line;
import subway.entity.LineEntity;

@Service
public class LineService {

    private final LineDao lineDao;

    public LineService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public Long createLine(final LineCreateRequest request) {
        final Line line = new Line(request.getName(), request.getColor());
        final LineEntity entity = new LineEntity(line.getId(), line.getName(), line.getColor());
        return lineDao.save(entity).getId();
    }

    public LineStationsResponse findLineById(final Long lineId) {
        return null;
    }

    public LinesResponse findLines() {
        return null;
    }

    public void createSection(final SectionCreateRequest request) {

    }

    public void deleteStation(final Long lineId, final Long stationId) {

    }
}

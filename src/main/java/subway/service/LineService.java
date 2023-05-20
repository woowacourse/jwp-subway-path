package subway.service;

import java.util.List;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineResponseWithStations;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SubwayMapService subwayMapService;

    public LineService(final LineDao lineDao, final SubwayMapService subwayMapService) {
        this.lineDao = lineDao;
        this.subwayMapService = subwayMapService;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponseWithStations> findLineResponses() {
        return subwayMapService.getLineResponsesWithStations();
    }

    public LineResponseWithStations findLineResponseById(final Long id) {
        return subwayMapService.getLineResponseWithStations(id);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }
}

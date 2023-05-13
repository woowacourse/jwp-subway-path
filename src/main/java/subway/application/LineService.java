package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dto.LineResponse;
import subway.entity.LineEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final StationDao stationDao;
    private final LineDao lineDao;

    public LineService(StationDao stationDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

//    public LineResponse saveLine(LineRequest request) {
//        LineEntity persistLine = lineDao.insert(new Line(request.getName(), request.getColor(), request));
//        return LineResponse.of(persistLine);
//    }

    public List<LineResponse> findLineResponses() {
        List<LineEntity> persistLines = lineDao.findAll();
        return persistLines.stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        LineEntity persistLine = lineDao.findById(id);
        return LineResponse.of(persistLine);
    }

//    public void updateLine(Long id, LineRequest lineUpdateRequest) {
//        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
//    }

    public void deleteLineById(Long id) {
        stationDao.deleteByLineId(id);
        lineDao.deleteById(id);
    }

}

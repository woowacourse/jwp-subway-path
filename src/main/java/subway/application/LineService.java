package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.PathDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.path.Path;
import subway.domain.path.Paths;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.PathRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final PathDao pathDao;

    public LineService(final LineDao lineDao, final StationDao stationDao, final PathDao pathDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.pathDao = pathDao;
    }

    @Transactional
    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor(), request.getAdditionalFare()));

        return LineResponse.from(persistLine);
    }

    public List<Line> findAllLines() {
        return lineDao.findAll().stream()
                .map(line -> {
                    final Paths paths = pathDao.findByLineId(line.getId());

                    return new Line(line.getId(), line.getName(), line.getColor(), line.getAdditionalFare(), paths);
                })
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(final Long id) {
        Line line = lineDao.findById(id);
        final Paths paths = pathDao.findByLineId(id);

        line = new Line(line.getId(), line.getName(), line.getColor(), line.getAdditionalFare(), paths);
        return LineResponse.from(line);
    }

    @Transactional
    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    @Transactional
    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }

    @Transactional
    public void addPathToLine(final Long lineId, final PathRequest pathRequest) {
        final Station upStation = stationDao.findById(pathRequest.getUpStationId());
        final Station downStation = stationDao.findById(pathRequest.getDownStationID());
        final Path newPath = new Path(upStation, downStation, pathRequest.getDistance());

        final Paths paths = pathDao.findByLineId(lineId);
        pathDao.save(paths.addPath(newPath), lineId);
    }

    @Transactional
    public void deletePath(final Long lineId, final Long stationId) {
        final Station station = stationDao.findById(stationId);
        final Paths paths = pathDao.findByLineId(lineId);

        pathDao.save(paths.removePath(station), lineId);
    }
}

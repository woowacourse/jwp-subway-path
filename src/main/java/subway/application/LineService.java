package subway.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineStationResponse;
import subway.dto.StationResponse;
import subway.entity.LineEntity;

@Service
@Transactional
public class LineService {
    private static final String NO_SUCH_ID_MESSAGE = "해당하는 ID가 없습니다.";

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Long save(final LineRequest request) {
        final Line line = new Line(request.getName(), request.getColor());
        return lineDao.insert(LineEntity.from(line));
    }

    public List<LineStationResponse> findAll() {
        final List<LineEntity> lines = lineDao.findAll();

        return lines.stream()
                .map(it -> findById(it.getId()))
                .collect(Collectors.toList());
    }

    public LineStationResponse findById(final Long id) {
        final LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_ID_MESSAGE));
        final List<Section> sections = sectionDao.findByLineId(id).stream()
                .map(it -> it.toDomain(
                        stationDao.findById(it.getUpStationId()).orElseThrow(NoSuchElementException::new).toDomain(),
                        stationDao.findById(it.getDownStationId()).orElseThrow(NoSuchElementException::new).toDomain()
                )).collect(Collectors.toList());
        final Line line = new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), new Sections(sections));

        final List<StationResponse> stationResponses = line.sortStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return LineStationResponse.from(LineResponse.of(lineEntity), stationResponses);
    }

    public void update(final Long id, final LineRequest lineUpdateRequest) {
        final Line line = new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        lineDao.update(id, LineEntity.from(line));
    }

    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }
}

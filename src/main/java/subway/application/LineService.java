package subway.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;
import subway.persistence.LineDao;
import subway.persistence.SectionDao;
import subway.persistence.StationDao;
import subway.persistence.dto.LineDto;
import subway.persistence.dto.SectionDto;
import subway.persistence.exception.NoSuchLineException;

@Service
public class LineService {

    private static final int ZERO = 0;

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Line saveLine(LineRequest request) {
        Line line = new Line(request.getName(), request.getColor());
        return save(line);
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = findBy(id);

        Line changed = line
                .changeName(lineRequest.getName())
                .changeColor(lineRequest.getColor());

        save(changed);
    }

    public void deleteLineById(Long id) {
        Line line = findBy(id);
        delete(line);
    }

    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = findBy(lineId);
        Station upper = stationDao.findById(sectionRequest.getUpperStationId());
        Station lower = stationDao.findById(sectionRequest.getLowerStationId());

        Distance distance = new Distance(sectionRequest.getDistance());
        Section section = new Section(upper, lower, distance);
        line.add(section);

        save(line);
    }

    public void deleteStation(Long lineId, Long stationId) {
        Line line = findBy(lineId);
        Station station = stationDao.findById(stationId);

        line.remove(station);

        save(line);
    }

    private Line save(Line line) {
        if (Objects.isNull(line.getId())) {
            return create(line);
        }
        return update(line);
    }

    @Transactional(readOnly = true)
    public Line findBy(Long id) {
        LineDto lineDto = lineDao.findById(id);
        return toLine(lineDto);
    }

    @Transactional(readOnly = true)
    public List<Line> findAll() {
        return lineDao.findAll().stream()
                .map(this::toLine)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Line line) {
        sectionDao.deleteAllByLineId(line.getId());
        int deletedCount = lineDao.deleteById(line.getId());
        validateChangedBy(deletedCount);
    }

    private void validateChangedBy(int affectedCount) {
        if (affectedCount == ZERO) {
            throw new NoSuchLineException();
        }
    }

    private Line create(Line line) {
        LineDto inserted = lineDao.insert(LineDto.of(line));
        saveSectionsOf(line);
        return putIdOn(line, inserted.getId());
    }

    private Line update(Line line) {
        saveSectionsOf(line);
        return line;
    }

    private void saveSectionsOf(Line line) {
        sectionDao.deleteAllByLineId(line.getId());
        sectionDao.insertAll(SectionDto.of(line.getId(), line.getSections()));
    }

    private List<Section> findSectionsOf(Long lineId) {
        return sectionDao.findAllByLineId(lineId)
                .stream()
                .map(this::toSection)
                .collect(Collectors.toList());
    }

    private Line putIdOn(Line line, Long id) {
        return new Line(
                id,
                line.getName(),
                line.getColor(),
                line.getSections()
        );
    }

    private Section toSection(SectionDto sectionDto) {
        return new Section(
                stationDao.findById(sectionDto.getStationId()),
                stationDao.findById(sectionDto.getNextStationId()),
                sectionDto.getDistance()
        );
    }

    private Line toLine(LineDto lineDto) {
        return new Line(
                lineDto.getId(),
                lineDto.getName(),
                lineDto.getColor(),
                findSectionsOf(lineDto.getId())
        );
    }
}

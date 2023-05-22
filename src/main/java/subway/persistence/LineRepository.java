package subway.persistence;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.Line;
import subway.domain.Section;
import subway.persistence.dto.LineDto;
import subway.persistence.dto.SectionDto;
import subway.persistence.exception.NoSuchLineException;

/* 학습용 주석입니다 :)
Repository 도입은 도메인 객체의 저장에 대한 관심사를 분리하기 위함이다.
저장 및 객체화를 캡슐화하므로 관심사가 분리된다.
덕분에 비즈니스 로직에서는 어떻게 저장하는지 알 필요가 없어 저장 및 객체화 로직이 최소화된다.
서비스 레이어에는 도메인 객체를 다루는 부분만 남아있으므로 유지보수가 용이해진다. (요구사항 변화에 유연하다)
*/
@Repository
public class LineRepository {

    private static final int ZERO = 0;

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineRepository(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    /* 학습용 주석입니다 :)
    호선의 식별자가 필요해지는 순간이다.
    '어느 호선'의 정보(이름, 색깔)로 저장할지?, '어느 호선'의 구간으로 저장할지? 결정할 때, 호선을 식별해야 하기 때문이다.
    이 Repository는 DB를 사용하므로 'id'를 바로 그 식별자로 사용했다.
    'id'를 도메인 객체에 포함시킨 것은 'id' 관리의 용이성을 취하고, 도메인 객체의 유연성(기술에 종속되지 않음)을 일부 희생한 것이다.
    객체와 별도로 관리하면 그것도 막을 수 있지만, 그것조차 하나의 프로젝트가 될 정도로 규모가 커지지 않을까?
    지금은 필요하지 않은 것 같다.
     */
    @Transactional
    public Line save(Line line) {
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
        LineDto inserted = lineDao.insert(toDto(line));
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

    private LineDto toDto(Line line) {
        return new LineDto(
                line.getId(),
                line.getName(),
                line.getColor()
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
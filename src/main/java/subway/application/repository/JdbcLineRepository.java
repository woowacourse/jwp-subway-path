package subway.application.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.entity.LineEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcLineRepository implements LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public JdbcLineRepository(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    @Override
    public Line saveLine(final Line line) {
        final LineEntity inserted = lineDao.insert(new LineEntity(line.getName(), line.getColor()));
        return new Line(inserted.getId(), inserted.getName(), inserted.getColor());
    }

    @Override
    public List<Line> findAllLines() {
        try {
            return lineDao.findAll().stream()
                    .map(lineEntity -> new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
                            new Sections(
                                    sectionDao.findByLineId(lineEntity.getId()).stream()
                                            .map(sectionEntity -> new Section(
                                                    new Station(sectionEntity.getUpStation()),
                                                    new Station(sectionEntity.getDownStation()),
                                                    sectionEntity.getDistance()))
                                            .collect(Collectors.toList())
                            )
                    ))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("존재하는 노선이 없습니다");
        }
    }

    @Override
    public Line findLineById(final Long id) {
        final LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 호선입니다"));
        try {
            return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
                    new Sections(
                            sectionDao.findByLineId(lineEntity.getId()).stream()
                                    .map(sectionEntity -> new Section(
                                            new Station(sectionEntity.getUpStation()),
                                            new Station(sectionEntity.getDownStation()),
                                            sectionEntity.getDistance()))
                                    .collect(Collectors.toList())
                    )
            );
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("노선에 존재하는 경로가 없습니다");
        }

    }

    @Override
    public void deleteLineById(final Long id) {
        try {
            lineDao.deleteById(id);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("제거하려는 노선이 존재하지 않습니다");
        }
    }
}

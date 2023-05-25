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
import subway.persistence.entity.SectionEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcSectionRepository implements SectionRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public JdbcSectionRepository(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    @Override
    public Sections findAllSections() {
        return new Sections(
                sectionDao.findAll().stream()
                        .map(sectionEntity -> new Section(new Station(sectionEntity.getUpStation()), new Station(sectionEntity.getDownStation()), sectionEntity.getDistance()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Line findLineById(final Sections sections, final Long id) {
        final LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 호선입니다"));
        return new Line(lineEntity.getName(), lineEntity.getColor(), sections);
    }

    @Override
    public void saveAllSection(final List<Section> sections, final Long lineId) {
        final List<SectionEntity> sectionEntities = sections.stream()
                .map(section -> new SectionEntity(
                        section.getUpStation().getName(),
                        section.getDownStation().getName(),
                        lineId,
                        section.getDistance()))
                .collect(Collectors.toList());
        sectionDao.insertAll(sectionEntities);
    }

    @Override
    public Sections findAllSectionByLineId(final Long lineId) {
        try {
            return new Sections(sectionDao.findByLineId(lineId).stream()
                    .map(sectionEntity -> new Section(
                            new Station(sectionEntity.getUpStation()),
                            new Station(sectionEntity.getDownStation()),
                            sectionEntity.getDistance()))
                    .collect(Collectors.toList()));
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("해당 노선의 경로가 없습니다");
        }
    }

    @Override
    public void deleteSections(final Long lineId, final List<Section> differentSections) {
        final List<SectionEntity> sectionEntities = differentSections.stream()
                .map(section -> new SectionEntity(section.getUpStation().getName(), section.getDownStation().getName(), lineId, section.getDistance()))
                .collect(Collectors.toList());
        sectionDao.deleteSections(sectionEntities);
    }
}

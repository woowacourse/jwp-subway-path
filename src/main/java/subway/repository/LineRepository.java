package subway.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.LineWithSectionEntities;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.SectionWithStationNameEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.exception.DuplicateException;
import subway.exception.ErrorCode;
import subway.exception.NotFoundException;

@Repository
public class LineRepository {
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Long save(final Line line) {
        boolean existName = lineDao.exists(line.getName());

        if (existName) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NAME);
        }

        return lineDao.save(toEntity(line));
    }

    public Line findById(final Long id) {
        LineEntity lineEntity = lineDao.findById(id);
        return Line.of(id, lineEntity.getName(), findSectionsByLineId(id));
    }

    private List<Section> findSectionsByLineId(final Long id) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);

        return sectionEntities.stream()
                .map(entity -> new Section(
                        findStationByStationId(entity.getUpStationId()),
                        findStationByStationId(entity.getDownStationId()),
                        entity.getDistance()
                )).collect(Collectors.toList());
    }

    private Station findStationByStationId(final Long stationId) {
        StationEntity stationEntity = stationDao.findById(stationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_STATION));

        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public List<Line> findAll() {
        List<LineWithSectionEntities> linesWithSections = lineDao.findLinesWithSections();
        List<Line> lines = new ArrayList<>();
        for (LineWithSectionEntities linesWithSection : linesWithSections) {
            List<SectionWithStationNameEntity> sectionEntities =
                    sectionDao.findByLineIdWithStationName(linesWithSection.getLineEntity().getId());

            List<Section> sections = sectionEntities.stream()
                    .map(section -> new Section(
                                    new Station(section.getUpStationEntity().getId(), section.getUpStationEntity().getName()),
                                    new Station(section.getDownStationEntity().getId(),
                                            section.getDownStationEntity().getName()),
                                    section.getSectionDistance()
                            )
                    ).collect(Collectors.toList());

            lines.add(Line.of(
                    linesWithSection.getLineEntity().getId(),
                    linesWithSection.getLineEntity().getName(),
                    sections)
            );
        }

        return lines;
    }

    private LineEntity toEntity(final Line line) {
        return new LineEntity(line.getName());
    }
}

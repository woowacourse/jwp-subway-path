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
import subway.exception.ErrorMessage;
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
            throw new DuplicateException(ErrorMessage.DUPLICATE_LINE);
        }

        return lineDao.save(toEntity(line));
    }

    public Line findById(final Long id) {
        LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_LINE));

        return Line.of(id, lineEntity.getName(), findSectionsByLineId(id));
    }

    private List<Section> findSectionsByLineId(final Long id) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);

        return sectionEntities.stream()
                .map(entity -> Section.of(
                        findStationByStationId(entity.getUpStationId()),
                        findStationByStationId(entity.getDownStationId()),
                        entity.getDistance()
                )).collect(Collectors.toList());
    }

    private Station findStationByStationId(final Long stationId) {
        StationEntity stationEntity = stationDao.findById(stationId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_STATION));

        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public List<Line> findAll() {
        List<LineWithSectionEntities> linesWithSections = lineDao.findLinesWithSections();   // section 리스트를 들고 있는 노선들
        List<Line> lines = new ArrayList<>();
        for (LineWithSectionEntities linesWithSection : linesWithSections) {    // 라인 하나 꺼내기
            List<SectionWithStationNameEntity> sectionEntities = linesWithSection.getSectionEntities().stream()
                    .filter(entity ->
                            entity.getUpStationId() != 0 && entity.getDownStationId() != 0)
                    .map(sectionEntity -> sectionDao.findBySectionIdWithStationName(sectionEntity.getId())
                            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_SECTION)))
                    .collect(Collectors.toList());

            List<Section> sections = sectionEntities.stream()
                    .map(section -> Section.of(
                            new Station(section.getUpStationEntity().getId(), section.getUpStationEntity().getName()),
                            new Station(section.getDownStationEntity().getId(),
                                    section.getDownStationEntity().getName()),
                            section.getSectionDistance()
                    ))
                    .collect(Collectors.toList());

            // 라인 리스트에 추가
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

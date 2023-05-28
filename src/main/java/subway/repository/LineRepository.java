package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.*;
import subway.entity.LineEntity;
import subway.entity.SectionDetailEntity;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public List<Line> findLinesWithSort() {
        List<SectionDetailEntity> sectionDetailEntities = sectionDao.findSectionsWithSort();
        return convertToLines(sectionDetailEntities);
    }

    public Line findLineWithSort(final Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 호선 ID가 없습니다."));
        List<Section> sections = sectionDao.findSectionsByLineIdWithSort(lineId)
                .stream()
                .map(this::convertToSection)
                .collect(Collectors.toList());
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), new Sections(sections));
    }

    private List<Line> convertToLines(final List<SectionDetailEntity> sectionDetailEntities) {
        Map<LineEntity, List<Section>> sectionsByLine = sectionDetailEntities.stream()
                .collect(Collectors.groupingBy(
                        sectionDetail -> new LineEntity(sectionDetail.getLineId(), sectionDetail.getLineName(), sectionDetail.getLineColor()),
                        Collectors.mapping(this::convertToSection, Collectors.toList())
                ));

        return sectionsByLine.keySet()
                .stream()
                .map(lineEntity -> new Line(
                        lineEntity.getId(),
                        lineEntity.getName(),
                        lineEntity.getColor(),
                        new Sections(sectionsByLine.get(lineEntity))))
                .collect(Collectors.toUnmodifiableList());
    }

    private Section convertToSection(final SectionDetailEntity sectionDetailEntity) {
        return new Section(
                new Station(sectionDetailEntity.getUpStationId(), sectionDetailEntity.getUpStationName()),
                new Station(sectionDetailEntity.getDownStationId(), sectionDetailEntity.getDownStationName()),
                sectionDetailEntity.getDistance(),
                sectionDetailEntity.getOrder()
        );
    }

}

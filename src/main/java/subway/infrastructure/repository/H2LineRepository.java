package subway.infrastructure.repository;

import org.springframework.stereotype.Repository;
import subway.application.port.LineRepository;
import subway.application.core.domain.Distance;
import subway.application.core.domain.Line;
import subway.application.core.domain.LineProperty;
import subway.application.core.domain.Section;
import subway.application.core.domain.Station;
import subway.infrastructure.dao.LineDao;
import subway.infrastructure.dao.SectionDao;
import subway.infrastructure.dao.StationDao;
import subway.infrastructure.entity.LineRow;
import subway.infrastructure.entity.SectionRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class H2LineRepository implements LineRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public H2LineRepository(SectionDao sectionDao, LineDao lineDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public Line findById(Long lineId) {
        List<SectionRow> sectionEntities = sectionDao.selectAllOfLineId(lineId);
        LineRow lineRow = lineDao.findById(lineId);
        List<Section> sections = generateSectionsFrom(sectionEntities);

        return new Line(new LineProperty(lineRow.getId(), lineRow.getName(),
                lineRow.getColor()), sections);
    }

    private List<Section> generateSectionsFrom(List<SectionRow> sectionEntities) {
        Map<Long, String> stationIdNameSet = getStationIdNameSet(sectionEntities);
        return sectionEntities.stream()
                .map(sectionRow -> new Section(
                        new Station(sectionRow.getUpBound(), stationIdNameSet.get(sectionRow.getUpBound())),
                        new Station(sectionRow.getDownBound(), stationIdNameSet.get(sectionRow.getDownBound())),
                        new Distance(sectionRow.getDistance())))
                .collect(Collectors.toList());
    }

    private Map<Long, String> getStationIdNameSet(List<SectionRow> sectionRows) {
        if (sectionRows.isEmpty()) {
            return Collections.emptyMap();
        }
        return stationDao.selectKeyValueSetWhereIdIn(getStationIds(sectionRows));
    }

    private List<Long> getStationIds(List<SectionRow> sectionEntities) {
        Set<Long> stationNames = new HashSet<>();
        for (SectionRow sectionRow : sectionEntities) {
            stationNames.add(sectionRow.getUpBound());
            stationNames.add(sectionRow.getDownBound());
        }
        return new ArrayList<>(stationNames);
    }

    public List<Line> findAll() {
        List<LineRow> lineRows = lineDao.selectAll();
        List<SectionRow> sectionRows = sectionDao.selectAll();
        Map<Long, String> stationIds = getStationIdNameSet(sectionRows);

        return lineRows.stream()
                .map(lineRow -> new Line(
                        new LineProperty(lineRow.getId(), lineRow.getName(), lineRow.getColor()),
                        findSectionsWithLineId(sectionRows, stationIds, lineRow)))
                .collect(Collectors.toList());
    }

    private List<Section> findSectionsWithLineId(List<SectionRow> sectionRows,
                                                 Map<Long, String> stationIds, LineRow lineRow) {
        return sectionRows.stream()
                .filter(sectionRow -> lineRow.getId().equals(sectionRow.getLineId()))
                .map(sectionRow -> new Section(
                        new Station(sectionRow.getUpBound(), stationIds.get(sectionRow.getUpBound())),
                        new Station(sectionRow.getDownBound(), stationIds.get(sectionRow.getDownBound())),
                        new Distance(sectionRow.getDistance())))
                .collect(Collectors.toList());
    }

    public Line insert(Line line) {
        List<SectionRow> sectionRows = line.getSections().stream()
                .map(section -> new SectionRow(null, line.getId(), section.getUpBound().getId(),
                        section.getDownBound().getId(), section.getDistance().value()))
                .collect(Collectors.toList());

        sectionDao.removeSections(line.getId());
        sectionDao.insertAll(sectionRows);
        return findById(line.getId());
    }
}

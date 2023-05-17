package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.domain.*;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;
import subway.ui.request.LineRequest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public LineRepository(final SectionDao sectionDao, final StationDao stationDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public long insert(final LineName lineName) {
        final LineEntity newLine = new LineEntity(lineName.getValue());
        return lineDao.insert(newLine);
    }

    public List<Line> findLines() {
        return lineDao.findAll().stream()
                .map(line -> findById(line.getId()))
                .collect(Collectors.toList());
    }

    public Line findById(final Long id) {
        final LineEntity line = lineDao.findById(id);
        final Sections sections = findByLineId(line.getId());
        return new Line(line.getId(), new LineName(line.getName()), sections);
    }

    private Sections findByLineId(final Long lineId) {
        final List<Section> sections = sectionDao.findByLineId(lineId).stream()
                .map(sectionEntity -> new Section(
                                sectionEntity.getId(),
                                new Station(sectionEntity.getBeforeStation()),
                                new Station(sectionEntity.getNextStation()),
                                new Distance(sectionEntity.getDistance())
                        )
                )
                .collect(Collectors.toList());

        // TODO: 정렬 로직 리팩터링
        final List<Section> sortedSections = getSortedSections(sections);

        final List<Long> stationIds = serializeToStationIds(sortedSections);
        final Map<Long, Station> stations = findStationsById(stationIds);
        return joinStationsToSections(sortedSections, stations);
    }

    private List<Section> getSortedSections(final List<Section> sections) {
        if (sections.size() == 0) {
            return new LinkedList<>();
        }

        final List<Section> sortedSections = new LinkedList<>();
        sortedSections.add(sections.get(0));
        while (sortedSections.size() < sections.size()) {
            final Station head = sortedSections.get(0).getBeforeStation();
            final Station tail = sortedSections.get(sortedSections.size() - 1).getNextStation();
            for (final Section section : sections) {
                if (section.getNextStation().equals(head)) {
                    sortedSections.add(0, section);
                    break;
                } else if (section.getBeforeStation().equals(tail)) {
                    sortedSections.add(section);
                    break;
                }
            }
        }
        return sortedSections;
    }

    private List<Long> serializeToStationIds(final List<Section> sections) {
        if (sections.isEmpty()) {
            return new LinkedList<>();
        }
        final List<Long> stationIds = sections.stream()
                .map(section -> section.getBeforeStation().getId())
                .collect(Collectors.toList());
        stationIds.add(sections.get(sections.size() - 1).getNextStation().getId());
        return stationIds;
    }

    private Map<Long, Station> findStationsById(final List<Long> stationIds) {
        if (stationIds.isEmpty()) {
            return new HashMap<>();
        }
        return stationDao.findAllById(stationIds).stream()
                .map(stationEntity -> new Station(stationEntity.getId(), stationEntity.getName()))
                .collect(Collectors.toMap(Station::getId, Function.identity()));
    }

    private Sections joinStationsToSections(final List<Section> sections, final Map<Long, Station> stations) {
        return new Sections(
                sections.stream()
                        .map(section -> new Section(
                                section.getId(),
                                stations.get(section.getBeforeStation().getId()),
                                stations.get(section.getNextStation().getId()),
                                section.getDistance()))
                        .collect(Collectors.toList())
        );
    }

    public void deleteSections(final Sections sections) {
        final List<Long> ids = sections.getSections().stream()
                .mapToLong(Section::getId)
                .boxed()
                .collect(Collectors.toList());
        sectionDao.delete(ids);
    }

    public void insertSections(final Long lineId, final Sections sections) {
        final List<SectionEntity> sectionEntities = sections.getSections().stream()
                .map(section -> new SectionEntity(
                        section.getBeforeStation().getId(),
                        section.getNextStation().getId(),
                        section.getDistance().getValue(),
                        lineId
                ))
                .collect(Collectors.toList());
        sectionDao.insert(lineId, sectionEntities);
    }

    public Station findStationByName(final String name) {
        return stationDao.findByName(name)
                .map(stationEntity -> new Station(stationEntity.getId(), stationEntity.getName()))
                .orElseThrow(() -> new IllegalArgumentException("역을 찾을 수 없습니다."));
    }

    public void updateName(final Long id, final LineRequest request) {
        final LineEntity line = new LineEntity(id, request.getName());
        lineDao.updateName(line);
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }
}

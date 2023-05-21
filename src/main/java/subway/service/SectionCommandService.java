package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.domain.station.Stations;
import subway.global.exception.section.CanNotDuplicatedSectionException;

import java.util.List;

@Service
public class SectionCommandService {

    private final SectionDao sectionDao;
    private final SectionQueryService sectionQueryService;

    public SectionCommandService(final SectionDao sectionDao, final SectionQueryService sectionQueryService) {
        this.sectionDao = sectionDao;
        this.sectionQueryService = sectionQueryService;
    }

    public void registerSection(
            final String currentStationName,
            final String nextStationName,
            final int distance,
            final Long lineId
    ) {

        final List<Section> originSections = sectionQueryService.searchSectionsByLineId(lineId);

        final Section targetSection = new Section(
                new Stations(
                        new Station(currentStationName),
                        new Station(nextStationName),
                        distance
                )
        );

        if (hasSameSection(targetSection, originSections)) {
            throw new CanNotDuplicatedSectionException("해당 호선에 이미 출발지와 도착지가 같은 구간이 존재합니다.");
        }

        final SectionEntity sectionEntity = new SectionEntity(
                currentStationName,
                nextStationName,
                distance,
                lineId
        );

        sectionDao.save(sectionEntity);
    }

    private boolean hasSameSection(final Section target, final List<Section> originSections) {
        return originSections.stream()
                             .anyMatch(section -> section.isSame(target));
    }

    public void deleteAll(final Long lineId) {
        sectionDao.deleteAll(lineId);
    }

    public void deleteSection(final Long sectionId) {
        sectionDao.deleteById(sectionId);
    }

    public void updateSection(final Section section) {

        final SectionEntity sectionEntity = new SectionEntity(
                section.getId(),
                section.getStations().getCurrent().getName(),
                section.getStations().getNext().getName(),
                section.getStations().getDistance()
        );

        sectionDao.update(sectionEntity);
    }
}

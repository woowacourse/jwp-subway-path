package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Stations;
import subway.global.exception.section.CanNotDuplicatedSectionException;
import subway.service.dto.SectionInLineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public void registerSection(
            final String currentStationName,
            final String nextStationName,
            final int distance,
            final Long lineId
    ) {

        final List<Section> originSections = findSectionsByLineId(lineId);

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
                             .anyMatch(it -> it.isSame(target));
    }

    public void deleteAll(final Long lineId) {
        sectionDao.deleteAll(lineId);
    }

    public void deleteSection(final Long sectionId) {
        sectionDao.deleteById(sectionId);
    }

    public List<SectionInLineResponse> mapToSectionInLineResponseFrom(final Line line) {
        return line.getSections()
                   .stream()
                   .map(it -> new SectionInLineResponse(
                           it.getStations().getCurrent().getName(),
                           it.getStations().getNext().getName(),
                           it.getStations().getDistance()))
                   .collect(Collectors.toList());
    }

    public List<Section> findSectionsByLineId(final Long lineId) {
        return sectionDao.findSectionsByLineId(lineId)
                         .stream()
                         .map(this::mapToSectionFrom)
                         .collect(Collectors.toList());
    }

    private Section mapToSectionFrom(final SectionEntity sectionEntity) {
        final Stations stations = new Stations(
                new Station(sectionEntity.getCurrentStationName()),
                new Station(sectionEntity.getNextStationName()),
                sectionEntity.getDistance()
        );

        return new Section(sectionEntity.getId(), stations);
    }

    public void updateSection(final Section section, final Long lineId) {

        final SectionEntity sectionEntity = new SectionEntity(
                section.getId(),
                section.getStations().getCurrent().getName(),
                section.getStations().getNext().getName(),
                section.getStations().getDistance(),
                lineId
        );

        sectionDao.update(sectionEntity);
    }
}

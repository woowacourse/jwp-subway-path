package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.persistence.dao.SectionDao;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationService stationService;

    public SectionService(final SectionDao sectionDao, final StationService stationService) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    public Sections findByLineId(final Long lineId) {
        final List<Section> sections = sectionDao.findByLineId(lineId);
        final List<Long> stationIds = serializeToStationIds(sections);
        final List<Station> stations = stationService.findStationsById(stationIds);
        return joinStationsToSections(sections, stations);
    }

    private static List<Long> serializeToStationIds(final List<Section> sections) {
        if (sections.isEmpty()) {
            return new LinkedList<>();
        }
        final List<Long> stationIds = sections.stream()
                .map(section -> section.getBeforeStation().getId())
                .collect(Collectors.toList());
        stationIds.add(sections.get(sections.size() - 1).getNextStation().getId());
        return stationIds;
    }

    private static Sections joinStationsToSections(final List<Section> sections, final List<Station> stations) {
        final List<Section> joinedSections = new LinkedList<>();
        for (int i = 0; i < sections.size(); i++) {
            final Station beforeStation = stations.get(i);
            final Station nextStation = stations.get(i + 1);
            final Section section = sections.get(i);
            joinedSections.add(new Section(section.getId(), beforeStation, nextStation, section.getDistance()));
        }
        return new Sections(joinedSections);
    }

    public void deleteSections(final Sections sections) {
        sectionDao.delete(sections.getSections());
    }

    public void insertSections(final Long lineId, final Sections sections) {
        sectionDao.insert(lineId, sections.getSections());
    }
}


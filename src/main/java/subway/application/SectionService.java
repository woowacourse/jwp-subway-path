package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionRepository;
import subway.domain.Section;
import subway.domain.SectionDirection;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionCreateRequest;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(final SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public Long create(final Long lineId, final SectionCreateRequest request) {
        final Sections sections = new Sections(sectionRepository.findAllByLineId(lineId));
        final Station baseStation = sectionRepository.registerStationIfNotExist(request.getBaseStation());
        final Station newStation = sectionRepository.registerStationIfNotExist(request.getNewStation());
        final Section newSection = Section.of(baseStation, newStation,
            SectionDirection.get(request.getDirection()), request.getDistance());

        sections.addSection(baseStation, newSection);
        updateSections(lineId, sections.getSections());

        return sectionRepository.findId(lineId, newSection);
    }


    private void updateSections(final Long lineId, final List<Section> sections) {
        sectionRepository.deleteAllByLineId(lineId);
        for (Section section : sections) {
            sectionRepository.create(lineId, section);
        }
    }

    @Transactional
    public void delete(final Long lineId, final String stationName) {
        final Sections sections = new Sections(sectionRepository.findAllByLineId(lineId));
        final Station deleteStation = sectionRepository.findStationByName(stationName);
        sections.removeStation(deleteStation);
        updateSections(lineId, sections.getSections());
    }
}

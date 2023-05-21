package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.SectionDto;
import subway.domain.*;
import subway.domain.repository.SectionRepository;
import subway.domain.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class SectionService {
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void saveSection(Long lineId, SectionDto sectionDto) {
        Station startStation = new Station(sectionDto.getStartStation());
        Station endStation = new Station(sectionDto.getEndStation());
        Distance distance = new Distance(sectionDto.getDistance());

        saveStation(startStation);
        saveStation(endStation);

        Section targetSection = new Section(startStation, endStation, distance);

        Sections sections = sectionRepository.findAllByLineId(lineId);
        sections.add(targetSection);

        updateSections(lineId, sections);
    }

    private void saveStation(Station station) {
        if (stationRepository.isExistStation(station)) {
            return;
        }

        stationRepository.save(station);
    }

    private void updateSections(Long lineId, Sections sections) {
        sectionRepository.deleteAllByLineId(lineId);
        sectionRepository.saveAll(lineId, sections);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Station targetStation = stationRepository.findById(stationId);
        Sections sections = sectionRepository.findAllByLineId(lineId);

        sections.remove(targetStation);

        updateSections(lineId, sections);
        deleteStation(stationId);
    }

    private void deleteStation(Long stationId) {
        if (sectionRepository.isExistSectionUsingStation(stationId)) {
            return;
        }

        stationRepository.deleteById(stationId);
    }
}

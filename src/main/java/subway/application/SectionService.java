package subway.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.dto.StationDto;
import subway.dto.StationsDto;
import subway.dto.request.SectionRequest;
import subway.repository.SectionRepository;

@Transactional
@Service
public class SectionService {
    private final SectionRepository sectionRepository;

    public SectionService(final SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public StationDto add(SectionRequest request, long lineId) {
        Sections rawSections = sectionRepository.findSectionsByLineId(lineId);
        Sections newSections = new Sections(rawSections.getSections());

        if (rawSections.isEmpty()) {
            return addFirstSection(lineId, request);
        }

        Section newSection = createSection(request);

        Station newStation = newSections.getNotIncludedStation(newSection);
        newSections.addSection(newSection);
        update(rawSections, newSections, lineId);

        Station savedStation = sectionRepository.findStationByName(newStation);
        return StationDto.from(savedStation);
    }

    private Section createSection(final SectionRequest request) {
        Station upStation = getStation(new Station(request.getUpStationName()));
        Station downStation = getStation(new Station(request.getDownStationName()));
        return Section.of(upStation, downStation, request.getDistance());
    }

    private Station getStation(Station station) {
        if (sectionRepository.existStationByName(station)) {
            return sectionRepository.findStationByName(station);
        }
        return sectionRepository.addStation(station);
    }

    private StationDto addFirstSection(final long lineId, final SectionRequest request) {
        Section section = Section.of(
                getStation(new Station(request.getUpStationName())),
                getStation(new Station(request.getDownStationName())),
                request.getDistance());
        Station station = sectionRepository.addSection(section, lineId);
        return StationDto.from(station);
    }

    private void update(final Sections rawSections, final Sections newSections,
                        final long lineId) {
        List<Section> deletedSections = getSubset(rawSections.getSections(), newSections.getSections());
        List<Section> addedSections = getSubset(newSections.getSections(), rawSections.getSections());
        sectionRepository.addSections(new Sections(addedSections), lineId);
        sectionRepository.deleteSections(new Sections(deletedSections), lineId);
    }

    public void delete(long stationId, long lineId) {
        Sections rawSections = sectionRepository.findSectionsByLineId(lineId);
        Sections newSections = new Sections(rawSections.getSections());
        Station station = sectionRepository.findStationById(stationId);
        newSections.deleteSectionByStation(station);
        update(rawSections, newSections, lineId);
    }

    private List<Section> getSubset(List<Section> sections, List<Section> other) {
        HashSet<Section> previousSections = new HashSet<>(sections);
        HashSet<Section> currentSections = new HashSet<>(other);
        previousSections.removeAll(currentSections);
        return new ArrayList<>(previousSections);
    }

    public StationsDto findAll(long lineId) {
        Sections sections = sectionRepository.findSectionsByLineId(lineId);
        return StationsDto.from(sections.findAllStation());
    }
}

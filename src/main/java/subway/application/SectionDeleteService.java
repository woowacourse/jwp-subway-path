package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;
import subway.ui.dto.SectionDeleteRequest;

@Service
public class SectionDeleteService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public SectionDeleteService(
            final LineRepository lineRepository,
            final SectionRepository sectionRepository,
            final StationRepository stationRepository
    ) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void deleteSection(SectionDeleteRequest deleteRequest) {
        Long lineId = deleteRequest.getLineId();
        Line line = findLine(lineId);
        Station station = findStation(deleteRequest.getStationName());

        if (!line.hasStation(station)) {
            throw new IllegalArgumentException("노선에 해당 역이 존재하지 않습니다.");
        }

        processSectionDeletion(line, station);
    }

    private Line findLine(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
    }

    private Station findStation(String name) {
        return stationRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 없습니다."));
    }

    private void processSectionDeletion(Line line, Station station) {
        if (line.hasOneSection()) {
            deleteSingleSection(line);
            return;
        }

        if (line.hasLeftStationInSection(station) && line.hasRightStationInSection(station)) {
            deleteMiddleSection(line, station);
            return;
        }

        if (line.isLastStationAtLeft(station)) {
            deleteLastSectionAtLeft(line, station);
        }

        if (line.isLastStationAtRight(station)) {
            deleteLastSectionAtRight(line, station);
        }
    }

    private void deleteSingleSection(Line line) {
        Section section = line.getSections().get(0);
        sectionRepository.delete(section.getLeft().getId(), section.getRight().getId());
    }

    private void deleteMiddleSection(Line line, Station station) {
        Section leftSection = line.findSectionByRightStation(station);
        Section rightSection = line.findSectionByLeftStation(station);

        sectionRepository.delete(leftSection.getLeft().getId(), leftSection.getRight().getId());
        sectionRepository.delete(rightSection.getLeft().getId(), rightSection.getRight().getId());

        int newDistance = leftSection.getDistance() + rightSection.getDistance();
        sectionRepository.save(line.getId(),
                new Section(leftSection.getLeft(), rightSection.getRight(), new Distance(newDistance)));
    }

    private void deleteLastSectionAtLeft(Line line, Station station) {
        Section section = line.findSectionByLeftStation(station);
        sectionRepository.delete(section.getLeft().getId(), section.getRight().getId());
    }

    private void deleteLastSectionAtRight(Line line, Station station) {
        Section section = line.findSectionByRightStation(station);
        sectionRepository.delete(section.getLeft().getId(), section.getRight().getId());
    }
}

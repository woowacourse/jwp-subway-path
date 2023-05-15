package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.dto.SectionEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.ui.dto.SectionDeleteRequest;

@Service
public class SectionDeleteService {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionDeleteService(SectionDao sectionDao, LineDao lineDao, LineRepository lineRepository,
                                StationRepository stationRepository) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void deleteSection(SectionDeleteRequest deleteRequest) {
        Station station = findStation(deleteRequest.getStationName());
        Line line = lineRepository.findById(deleteRequest.getLineId());

        if (!line.hasStation(station)) {
            throw new IllegalArgumentException("노선에 해당 역이 존재하지 않습니다.");
        }

        if (line.hasOneSection()) {
            Section section = line.getSections().get(0);
            sectionDao.deleteByStationId(section.getLeft().getId(), section.getRight().getId());
            return;
        }

        processSectionDeletion(line, station);
    }

    private Station findStation(String name) {
        return stationRepository.findByName(name);
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
        sectionDao.deleteByStationId(section.getLeft().getId(), section.getRight().getId());
    }

    private void deleteMiddleSection(Line line, Station station) {
        Section leftSection = line.findSectionByRightStation(station);
        Section rightSection = line.findSectionByLeftStation(station);

        sectionDao.deleteByStationId(leftSection.getLeft().getId(), leftSection.getRight().getId());
        sectionDao.deleteByStationId(rightSection.getLeft().getId(), rightSection.getRight().getId());

        int newDistance = leftSection.getDistance() + rightSection.getDistance();
        sectionDao.insert(
                new SectionEntity(line.getId(), leftSection.getLeft().getId(), rightSection.getRight().getId(),
                        newDistance));
    }

    private void deleteLastSectionAtLeft(Line line, Station station) {
        Section section = line.findSectionByLeftStation(station);
        sectionDao.deleteByStationId(section.getLeft().getId(), section.getRight().getId());
    }

    private void deleteLastSectionAtRight(Line line, Station station) {
        Section section = line.findSectionByRightStation(station);
        sectionDao.deleteByStationId(section.getLeft().getId(), section.getRight().getId());
    }
}

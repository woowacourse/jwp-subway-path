package subway.application;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.SectionDto;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.dto.LineDto;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.ui.dto.SectionDeleteRequest;

@Service
public class SectionDeleteService {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public SectionDeleteService(SectionDao sectionDao, LineDao lineDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    @Transactional
    public void deleteSection(SectionDeleteRequest deleteRequest) {
        Long lineId = deleteRequest.getLineId();
        LineDto foundLine = findLine(lineId);
        Station station = findStation(deleteRequest.getStationName());
        Line line = makeLine(lineId, foundLine);

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

    private LineDto findLine(Long lineId) {
        return lineDao.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
    }

    private Line makeLine(Long lineId, LineDto foundLine) {
        List<SectionDto> sectionDtos = sectionDao.findByLineId(lineId);
        LinkedList<Section> sections = sectionDtos.stream()
                .map(sectionDto -> new Section(
                                stationDao.findById(sectionDto.getLeftStationId()),
                                stationDao.findById(sectionDto.getRightStationId()),
                                new Distance(sectionDto.getDistance())
                        )
                ).collect(Collectors.toCollection(LinkedList::new));

        return new Line(foundLine.getId(), foundLine.getName(), sections);
    }

    private Station findStation(String name) {
        return stationDao.findByName(name)
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
        sectionDao.deleteByStationId(section.getLeft().getId(), section.getRight().getId());
    }

    private void deleteMiddleSection(Line line, Station station) {
        Section leftSection = line.findSectionByRightStation(station);
        Section rightSection = line.findSectionByLeftStation(station);

        sectionDao.deleteByStationId(leftSection.getLeft().getId(), leftSection.getRight().getId());
        sectionDao.deleteByStationId(rightSection.getLeft().getId(), rightSection.getRight().getId());

        int newDistance = leftSection.getDistance() + rightSection.getDistance();
        sectionDao.insert(new SectionDto(line.getId(), leftSection.getLeft().getId(), rightSection.getRight().getId(),
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

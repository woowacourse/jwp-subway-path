package subway.domain;

import subway.dto.SectionChange;
import subway.exceptions.customexceptions.InvalidDataException;
import subway.exceptions.customexceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {
    private List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
        sort();
    }

    private void sort() {
        if (sections.isEmpty()) {
            return;
        }
        List<Section> sortedSections = new ArrayList<>();
        Section upEndPointSection = findUpEndPointSection();
        Section downEndPointSection = findDownEndPointSection();
        Section currentSection = upEndPointSection;

        sortedSections.add(upEndPointSection);

        while (!currentSection.equals(downEndPointSection)) {
            Section nextSection = findNextSection(currentSection);
            sortedSections.add(nextSection);
            currentSection = nextSection;
        }
        sections = sortedSections;
    }

    private Section findUpEndPointSection() {
        List<Long> upBoundStationsId = sections.stream()
                .mapToLong(Section::getUpBoundStationId)
                .boxed()
                .collect(Collectors.toList());
        List<Long> downBoundStationsId = sections.stream()
                .mapToLong(Section::getDownBoundStationId)
                .boxed()
                .collect(Collectors.toList());
        upBoundStationsId.removeAll(downBoundStationsId);
        return sections.stream()
                .filter(section -> section.getUpBoundStationId().equals(upBoundStationsId.get(0)))
                .findAny()
                .orElseThrow(() -> new NotFoundException("상행 종점을 찾을 수 없습니다."));
    }

    private Section findDownEndPointSection() {
        List<Long> upBoundStationsId = sections.stream()
                .mapToLong(Section::getUpBoundStationId)
                .boxed()
                .collect(Collectors.toList());
        List<Long> downBoundStationsId = sections.stream()
                .mapToLong(Section::getDownBoundStationId)
                .boxed()
                .collect(Collectors.toList());
        downBoundStationsId.removeAll(upBoundStationsId);
        return sections.stream()
                .filter(section -> section.getDownBoundStationId().equals(downBoundStationsId.get(0)))
                .findAny()
                .orElseThrow(() -> new NotFoundException("하행 종점을 찾을 수 없습니다."));
    }

    private Section findNextSection(Section currentSection) {
        return sections.stream()
                .filter(section -> currentSection.getDownBoundStationId().equals(section.getUpBoundStationId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("다음 역 간 연결 정보를 찾을 수 없습니다."));
    }

    public SectionChange addSection(Section newSection) {
        if (sections.isEmpty()) {
            return addSectionFirst(newSection);
        }
        if (existBothStationAlready(newSection)) {
            throw new InvalidDataException("두 역이 이미 노선에 존재합니다.");
        }
        if (existDownBoundStation(newSection)) {
            return addSectionToUp(newSection);
        }
        if (existUpBoundStation(newSection)) {
            return addSectionToDown(newSection);
        }
        throw new InvalidDataException("노선에 이미 역들이 존재해 두개의 역을 추가할 수 없습니다.");
    }

    private SectionChange addSectionFirst(Section newSection) {
        SectionChange sectionChange = new SectionChange();
        sectionChange.addNewSection(newSection);
        return sectionChange;
    }

    private boolean existBothStationAlready(Section newSection) {
        return existUpBoundStation(newSection) && existDownBoundStation(newSection);
    }

    private boolean existUpBoundStation(Section newSection) {
        Long upBoundStationId = newSection.getUpBoundStationId();
        return sections.stream()
                .anyMatch(section -> section.getUpBoundStationId().equals(upBoundStationId) ||
                        section.getDownBoundStationId().equals(upBoundStationId));
    }

    private boolean existDownBoundStation(Section newSection) {
        Long downBoundStationId = newSection.getDownBoundStationId();
        return sections.stream()
                .anyMatch(section -> section.getUpBoundStationId().equals(downBoundStationId) ||
                        section.getDownBoundStationId().equals(downBoundStationId));
    }

    private SectionChange addSectionToUp(Section newSection) {
        Long existStationId = newSection.getDownBoundStationId();
        Section upEndPointSection = findUpEndPointSection();
        SectionChange sectionChange = new SectionChange();

        if (existStationId.equals(upEndPointSection.getUpBoundStationId())) {
            sectionChange.addNewSection(newSection);
            return sectionChange;
        }
        Section upBoundSection = findUpBoundSection(existStationId);
        validateDistance(upBoundSection.getDistance(), newSection.getDistance());

        Section updatedSection = new Section(upBoundSection.getId(),
                upBoundSection.getUpBoundStationId(),
                newSection.getUpBoundStationId(),
                upBoundSection.getLineId(),
                upBoundSection.getDistance() - newSection.getDistance()
        );
        sectionChange.addNewSection(newSection);
        sectionChange.addUpdatedSection(updatedSection);
        return sectionChange;
    }

    private SectionChange addSectionToDown(Section newSection) {
        Long existStationId = newSection.getUpBoundStationId();
        Section downEndPointSection = findDownEndPointSection();
        SectionChange sectionChange = new SectionChange();

        if (existStationId.equals(downEndPointSection.getDownBoundStationId())) {
            sectionChange.addNewSection(newSection);
            return sectionChange;
        }
        Section downBoundSection = findDownBoundSection(existStationId);
        validateDistance(downBoundSection.getDistance(), newSection.getDistance());

        Section updatedSection = new Section(downBoundSection.getId(),
                newSection.getDownBoundStationId(),
                downBoundSection.getDownBoundStationId(),
                downBoundSection.getLineId(),
                downBoundSection.getDistance() - newSection.getDistance());
        sectionChange.addNewSection(newSection);
        sectionChange.addUpdatedSection(updatedSection);
        return sectionChange;
    }

    private Section findUpBoundSection(Long downBoundStationId) {
        return sections.stream()
                .filter(section -> section.getDownBoundStationId().equals(downBoundStationId))
                .findAny()
                .orElseThrow(() -> new NotFoundException("올바른 역을 찾을 수 없습니다."));
    }

    private Section findDownBoundSection(Long upBoundStationId) {
        return sections.stream()
                .filter(section -> section.getUpBoundStationId().equals(upBoundStationId))
                .findAny()
                .orElseThrow(() -> new NotFoundException("올바른 역을 찾을 수 없습니다."));
    }

    private void validateDistance(int originDistance, int newDistance) {
        if (newDistance >= originDistance) {
            throw new InvalidDataException("원래 역 사이의 거리보다 추가 할 역사이의 거리가 크거나 같습니다.");
        }
    }

    public SectionChange removeStation(Long stationId) {
        if (sections.isEmpty()) {
            throw new InvalidDataException("노선에 아무 역도 존재하지 않습니다.");
        }

        if (notExistInLine(stationId)) {
            throw new InvalidDataException("해당 역이 노선에 존재하지 않습니다.");
        }

        Section upEndPointSection = findUpEndPointSection();
        Section downEndPointSection = findDownEndPointSection();

        if (upEndPointSection.getUpBoundStationId().equals(stationId)) {
            SectionChange sectionChange = new SectionChange();
            sectionChange.addDeletedSection(upEndPointSection);
            return sectionChange;
        }
        if (downEndPointSection.getDownBoundStationId().equals(stationId)) {
            SectionChange sectionChange = new SectionChange();
            sectionChange.addDeletedSection(downEndPointSection);
            return sectionChange;
        }
        return removeMiddleStation(stationId);
    }

    private boolean notExistInLine(Long stationId) {
        return sections.stream()
                .noneMatch(section -> section.getUpBoundStationId().equals(stationId) ||
                        section.getDownBoundStationId().equals(stationId));
    }

    private SectionChange removeMiddleStation(Long stationId) {
        Section upBoundSection = findUpBoundSection(stationId);
        Section downBoundSection = findDownBoundSection(stationId);

        SectionChange sectionChange = new SectionChange();

        int newDistance = upBoundSection.getDistance() + downBoundSection.getDistance();
        Section updatedSection = new Section(upBoundSection.getId(),
                upBoundSection.getUpBoundStationId(),
                downBoundSection.getDownBoundStationId(),
                newDistance);

        sectionChange.addUpdatedSection(updatedSection);
        sectionChange.addDeletedSection(downBoundSection);
        return sectionChange;
    }

    public List<Long> getStationsWithUpToDownDirection() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> stations = new ArrayList<>();
        for (Section section : sections) {
            stations.add(section.getUpBoundStationId());
        }
        Section lastSection = sections.get(sections.size() - 1);
        stations.add(lastSection.getDownBoundStationId());
        return stations;
    }
}

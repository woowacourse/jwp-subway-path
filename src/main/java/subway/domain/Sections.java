package subway.domain;

import subway.exceptions.customexceptions.InvalidDataException;
import subway.exceptions.customexceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private List<Section> sections;
    private final SectionChange sectionChange;

    public Sections(List<Section> sections) {
        this.sections = sections;
        sort();
        sectionChange = new SectionChange();
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
                .filter(section -> currentSection.isUpSection(section.getUpBoundStationId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("다음 역 간 연결 정보를 찾을 수 없습니다."));
    }

    public void addSection(Section newSection) {
        if (sections.isEmpty()) {
            addSectionFirst(newSection);
            return;
        }
        if (existBothStationAlready(newSection)) {
            throw new InvalidDataException("두 역이 이미 노선에 존재합니다.");
        }
        if (existDownBoundStation(newSection)) {
            addSectionToUp(newSection);
            return;
        }
        if (existUpBoundStation(newSection)) {
            addSectionToDown(newSection);
            return;
        }
        throw new InvalidDataException("노선에 이미 역들이 존재해 두개의 역을 추가할 수 없습니다.");
    }

    private void addSectionFirst(Section newSection) {
        sectionChange.addNewSection(newSection);
    }

    private boolean existBothStationAlready(Section newSection) {
        return existUpBoundStation(newSection) && existDownBoundStation(newSection);
    }

    private boolean existUpBoundStation(Section newSection) {
        Long upBoundStationId = newSection.getUpBoundStationId();

        return sections.stream()
                .anyMatch(section -> section.isDownSection(upBoundStationId) ||
                        section.isUpSection(upBoundStationId));
    }

    private boolean existDownBoundStation(Section newSection) {
        Long downBoundStationId = newSection.getDownBoundStationId();

        return sections.stream()
                .anyMatch(section -> section.isDownSection(downBoundStationId) ||
                        section.isUpSection(downBoundStationId));
    }

    private void addSectionToUp(Section newSection) {
        Long existStationId = newSection.getDownBoundStationId();
        Section upEndPointSection = findUpEndPointSection();

        if (upEndPointSection.isDownSection(existStationId)) {
            sectionChange.addNewSection(newSection);
            return;
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
    }

    private void addSectionToDown(Section newSection) {
        Long existStationId = newSection.getUpBoundStationId();
        Section downEndPointSection = findDownEndPointSection();

        if (downEndPointSection.isUpSection(existStationId)) {
            sectionChange.addNewSection(newSection);
            return;
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
    }

    private Section findUpBoundSection(Long downBoundStationId) {
        return sections.stream()
                .filter(section -> section.isUpSection(downBoundStationId))
                .findAny()
                .orElseThrow(() -> new NotFoundException("올바른 역을 찾을 수 없습니다."));

    }

    private Section findDownBoundSection(Long upBoundStationId) {
        return sections.stream()
                .filter(section -> section.isDownSection(upBoundStationId))
                .findAny()
                .orElseThrow(() -> new NotFoundException("올바른 역을 찾을 수 없습니다."));
    }

    private void validateDistance(int originDistance, int newDistance) {
        if (newDistance >= originDistance) {
            throw new InvalidDataException("원래 역 사이의 거리보다 추가 할 역사이의 거리가 크거나 같습니다.");
        }
    }

    public void removeStation(Long stationId) {
        if (sections.isEmpty()) {
            throw new InvalidDataException("노선에 아무 역도 존재하지 않습니다.");
        }
        if (notExistInLine(stationId)) {
            throw new InvalidDataException("해당 역이 노선에 존재하지 않습니다.");
        }

        Section upEndPointSection = findUpEndPointSection();
        Section downEndPointSection = findDownEndPointSection();

        if (upEndPointSection.isDownSection(stationId)) {
            sectionChange.addDeletedSection(upEndPointSection);
            return;
        }
        if (downEndPointSection.isUpSection(stationId)) {
            sectionChange.addDeletedSection(downEndPointSection);
            return;
        }
        removeMiddleStation(stationId);
    }

    private boolean notExistInLine(Long stationId) {
        return sections.stream()
                .noneMatch(section -> section.isDownSection(stationId) ||
                        section.isUpSection(stationId));
    }

    private void removeMiddleStation(Long stationId) {
        Section upBoundSection = findUpBoundSection(stationId);
        Section downBoundSection = findDownBoundSection(stationId);

        int newDistance = upBoundSection.getDistance() + downBoundSection.getDistance();
        Section updatedSection = new Section(upBoundSection.getId(),
                upBoundSection.getUpBoundStationId(),
                downBoundSection.getDownBoundStationId(),
                newDistance);

        sectionChange.addUpdatedSection(updatedSection);
        sectionChange.addDeletedSection(downBoundSection);
    }

    public void reflectSectionChange() {
        for (Section section : sectionChange.getUpdatedSections()) {
            for (int idx = 0; idx < sections.size(); idx++) {
                if (section.equals(sections.get(idx))) {
                    sections.set(idx, section);
                }
            }
        }
        for (Section section : sectionChange.getNewSections()) {
            sections.add(section);
        }
        for (Section section : sectionChange.getDeletedSections()) {
            for (int idx = 0; idx < sections.size(); idx++) {
                if (section.equals(sections.get(idx))) {
                    sections.remove(idx);
                }
            }
        }
        sort();
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

    public SectionChange findSectionChange() {
        return sectionChange;
    }
}

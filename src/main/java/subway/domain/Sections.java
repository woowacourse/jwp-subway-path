package subway.domain;

import subway.dto.AddResult;
import subway.dto.RemoveResult;
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
        List<Station> upBoundStations = sections.stream()
                .map(Section::getUpBoundStation)
                .collect(Collectors.toList());
        List<Station> downBoundStations = sections.stream()
                .map(Section::getDownBoundStation)
                .collect(Collectors.toList());
        upBoundStations.removeAll(downBoundStations);
        return sections.stream()
                .filter(section -> section.getUpBoundStation().equals(upBoundStations.get(0)))
                .findAny()
                .orElseThrow(() -> new NotFoundException("상행 종점을 찾을 수 없습니다."));
    }

    private Section findDownEndPointSection() {
        List<Station> upBoundStations = sections.stream()
                .map(Section::getUpBoundStation)
                .collect(Collectors.toList());
        List<Station> downBoundStations = sections.stream()
                .map(Section::getDownBoundStation)
                .collect(Collectors.toList());
        downBoundStations.removeAll(upBoundStations);
        return sections.stream()
                .filter(section -> section.getDownBoundStation().equals(downBoundStations.get(0)))
                .findAny()
                .orElseThrow(() -> new NotFoundException("하행 종점을 찾을 수 없습니다."));
    }

    private Section findNextSection(Section currentSection) {
        return sections.stream()
                .filter(section -> currentSection.getDownBoundStation().equals(section.getUpBoundStation()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("다음 역 간 연결 정보를 찾을 수 없습니다."));
    }

    public AddResult addSection(Section newSection) {
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

    private AddResult addSectionFirst(Section newSection) {
        AddResult addResult = new AddResult();
        addResult.insertSectionToAddSections(newSection);
        return addResult;
    }

    private boolean existBothStationAlready(Section newSection) {
        return existUpBoundStation(newSection) && existDownBoundStation(newSection);
    }

    private boolean existUpBoundStation(Section newSection) {
        Station newUpBoundStation = newSection.getUpBoundStation();
        return sections.stream()
                .anyMatch(section -> section.getUpBoundStation().equals(newUpBoundStation) ||
                        section.getDownBoundStation().equals(newUpBoundStation));
    }

    private boolean existDownBoundStation(Section newSection) {
        Station newDownBoundStation = newSection.getDownBoundStation();
        return sections.stream()
                .anyMatch(section -> section.getUpBoundStation().equals(newDownBoundStation) ||
                        section.getDownBoundStation().equals(newDownBoundStation));
    }

    private AddResult addSectionToUp(Section newSection) {
        Station existStation = newSection.getDownBoundStation();
        Section upEndPointSection = findUpEndPointSection();

        if (existStation.equals(upEndPointSection.getUpBoundStation())) {
            AddResult addResult = new AddResult();
            addResult.insertSectionToAddSections(newSection);
            return addResult;
        }
        Section upBoundSection = findUpBoundSection(existStation);
        validateDistance(upBoundSection.getDistance(), newSection.getDistance());

        Section updatedSection = new Section(upBoundSection.getId(),
                upBoundSection.getUpBoundStation(),
                newSection.getUpBoundStation(),
                upBoundSection.getDistance() - newSection.getDistance()
        );
        AddResult addResult = new AddResult();
        addResult.insertSectionToAddSections(newSection);
        addResult.insertSectionToUpdateSections(updatedSection);
        return addResult;
    }

    private AddResult addSectionToDown(Section newSection) {
        Station existStation = newSection.getUpBoundStation();
        Section downEndPointSection = findDownEndPointSection();

        if (existStation.equals(downEndPointSection.getDownBoundStation())) {
            AddResult addResult = new AddResult();
            addResult.insertSectionToAddSections(newSection);
            return addResult;
        }
        Section downBoundSection = findDownBoundSection(existStation);
        validateDistance(downBoundSection.getDistance(), newSection.getDistance());

        Section updatedSection = new Section(downBoundSection.getId(),
                newSection.getDownBoundStation(),
                downBoundSection.getDownBoundStation(),
                downBoundSection.getDistance() - newSection.getDistance());
        AddResult addResult = new AddResult();
        addResult.insertSectionToAddSections(newSection);
        addResult.insertSectionToUpdateSections(updatedSection);
        return addResult;
    }

    private Section findUpBoundSection(Station station) {
        return sections.stream()
                .filter(section -> section.getDownBoundStation().equals(station))
                .findAny()
                .orElseThrow(() -> new NotFoundException("올바른 역을 찾을 수 없습니다."));
    }

    private Section findDownBoundSection(Station station) {
        return sections.stream()
                .filter(section -> section.getUpBoundStation().equals(station))
                .findAny()
                .orElseThrow(() -> new NotFoundException("올바른 역을 찾을 수 없습니다."));
    }

    private void validateDistance(int originDistance, int newDistance) {
        if (newDistance >= originDistance) {
            throw new InvalidDataException("원래 역 사이의 거리보다 추가 할 역사이의 거리가 크거나 같습니다.");
        }
    }

    public RemoveResult removeStation(Station station) {
        if (sections.isEmpty()) {
            throw new InvalidDataException("노선에 아무 역도 존재하지 않습니다.");
        }

        if (notExistInLine(station)) {
            throw new InvalidDataException("해당 역이 노선에 존재하지 않습니다.");
        }

        Section upEndPointSection = findUpEndPointSection();
        Section downEndPointSection = findDownEndPointSection();

        if (upEndPointSection.getUpBoundStation().equals(station)) {
            RemoveResult removeResult = new RemoveResult();
            removeResult.insertIdToRemoveIds(upEndPointSection.getId());
            return removeResult;
        }
        if (downEndPointSection.getDownBoundStation().equals(station)) {
            RemoveResult removeResult = new RemoveResult();
            removeResult.insertIdToRemoveIds(downEndPointSection.getId());
            return removeResult;
        }
        return removeMiddleStation(station);
    }

    private boolean notExistInLine(Station station) {
        return sections.stream()
                .noneMatch(section -> section.getUpBoundStation().equals(station) ||
                        section.getDownBoundStation().equals(station));
    }

    private RemoveResult removeMiddleStation(Station station) {
        Section upBoundSection = findUpBoundSection(station);
        Section downBoundSection = findDownBoundSection(station);

        RemoveResult removeResult = new RemoveResult();

        int newDistance = upBoundSection.getDistance() + downBoundSection.getDistance();
        Section updatedSection = new Section(upBoundSection.getId(),
                upBoundSection.getUpBoundStation(),
                downBoundSection.getDownBoundStation(),
                newDistance);

        removeResult.insertSectionToUpdateSections(updatedSection);
        removeResult.insertIdToRemoveIds(downBoundSection.getId());
        return removeResult;
    }

    public List<Station> getStationsWithUpToDownDirection() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        for (Section section : sections) {
            stations.add(section.getUpBoundStation());
        }
        Section lastSection = sections.get(sections.size() - 1);
        stations.add(lastSection.getDownBoundStation());
        return stations;
    }
}

package subway.domain;

import subway.exception.InvalidSectionException;
import subway.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class Sections {

    public static final Sections EMPTY_SECTION = new Sections(Collections.EMPTY_LIST);

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public List<Station> findStations() {
        List<Station> upStations = getUpStations();
        List<Station> downStations = getDownStations();
        Optional<Station> startStation = findStartStation(upStations, downStations);

        if (startStation.isEmpty()) {
            return null;
        }

        List<Station> stations = new LinkedList<>(List.of(startStation.get()));
        while (stations.size() <= upStations.size()) {
            stations.add(sort(stations));
        }

        return stations;
    }

    private List<Station> getUpStations() {
        return sections.stream()
                       .map(Section::getUpStation)
                       .collect(Collectors.toList());
    }

    private List<Station> getDownStations() {
        return sections.stream()
                       .map(Section::getDownStation)
                       .collect(Collectors.toList());
    }

    private static Optional<Station> findStartStation(List<Station> upStations, List<Station> downStations) {
        return upStations.stream()
                         .filter(station -> !downStations.contains(station))
                         .findFirst();
    }

    private Station sort(List<Station> stations) {
        return sections.stream()
                       .filter(section -> stations.get(stations.size() - 1)
                                                  .equals(section.getUpStation()))
                       .map(Section::getDownStation)
                       .findFirst()
                       .orElseThrow(() -> new NotFoundException("다음 역을 찾을 수 없습니다."));
    }

    public void addSection(Section sectionToAdd) {
        Station upStation = sectionToAdd.getUpStation();
        Station downStation = sectionToAdd.getDownStation();
        checkIfImpossibleSection(sectionToAdd);
        addStationBetweenStations(sectionToAdd, upStation, downStation);
    }

    private void checkIfImpossibleSection(Section sectionToAdd) {
        boolean hasUpStationInLine = hasStationInLine(sectionToAdd.getUpStation());
        boolean hasDownStationInLine = hasStationInLine(sectionToAdd.getDownStation());

        if (hasUpStationInLine && hasDownStationInLine) {
            throw new InvalidSectionException("이미 존재하는 구간입니다.");
        }
    }

    private boolean hasStationInLine(Station newStation) {
        boolean isInUpStation = sections.stream()
                                        .anyMatch(section -> section.isUpStation(newStation));
        boolean isInDownStation = sections.stream()
                                          .anyMatch(section -> section.isDownStation(newStation));

        return isInUpStation || isInDownStation;
    }

    private void addStationBetweenStations(Section sectionToAdd, Station upStation, Station downStation) {
        if (hasStationInLine(upStation)) {
            addSectionBasedOnUpStation(sectionToAdd, upStation);
            return;
        }
        if (hasStationInLine(downStation)) {
            addSectionBasedOnDownStation(sectionToAdd, downStation);
            return;
        }
        throw new InvalidSectionException("한 역은 기존의 노선에 존재해야 합니다.");
    }

    private void addSectionBasedOnUpStation(Section sectionToAdd, Station upStation) {
        sections.stream()
                .filter(section -> section.isUpStation(upStation))
                .findFirst()
                .ifPresent(originalSection -> {
                            Station downStationIdOfOrigin = originalSection.getDownStation();
                            Station downStationIdOfToAdd = sectionToAdd.getDownStation();
                            Integer revisedDistance = findRevisedDistance(sectionToAdd, originalSection);
                            Section revisedSection = Section.of(originalSection.getId(), downStationIdOfToAdd, downStationIdOfOrigin, revisedDistance);
                            sections.remove(originalSection);
                            sections.add(sectionToAdd);
                            sections.add(revisedSection);
                        }
                );

        sections.stream()
                .filter(section -> section.isDownStation(upStation))
                .findFirst()
                .ifPresent(section -> sections.add(sectionToAdd));
    }

    private void addSectionBasedOnDownStation(Section sectionToAdd, Station downStation) {
        sections.stream()
                .filter(section -> section.isUpStation(downStation))
                .findFirst()
                .ifPresent(section -> sections.add(sectionToAdd));

        sections.stream()
                .filter(section -> section.isDownStation(downStation))
                .findFirst()
                .ifPresent(originalSection -> {
                    Station upStationIdOfOrigin = originalSection.getUpStation();
                    Station upStationIdOfToAdd = sectionToAdd.getUpStation();
                    Integer revisedDistance = findRevisedDistance(sectionToAdd, originalSection);
                    Section revisedSection = Section.of(originalSection.getId(), upStationIdOfOrigin, upStationIdOfToAdd, revisedDistance);
                    sections.remove(originalSection);
                    sections.add(sectionToAdd);
                    sections.add(revisedSection);
                });
    }

    private int findRevisedDistance(Section sectionToAdd, Section originalSection) {
        int revisedDistance = originalSection.getDistance() - sectionToAdd.getDistance();
        if (revisedDistance <= 0) {
            throw new InvalidSectionException("현재 구간보다 큰 구간은 입력할 수 없습니다.");
        }
        return revisedDistance;
    }

    public void removeStation(Station removeStation) {
        if (hasStationBothInLine(removeStation)) {
            deleteStationBetweenStations(removeStation);
            return;
        }

        sections.stream()
                .filter(section -> section.isUpStation(removeStation))
                .findFirst()
                .ifPresent(sections::remove);

        sections.stream()
                .filter(section -> section.isDownStation(removeStation))
                .findFirst()
                .ifPresent(sections::remove);
    }

    private boolean hasStationBothInLine(Station newStation) {
        boolean isInUpStation = sections.stream()
                                        .anyMatch(section -> section.isUpStation(newStation));
        boolean isInDownStation = sections.stream()
                                          .anyMatch(section -> section.isDownStation(newStation));

        return isInUpStation && isInDownStation;
    }

    private void deleteStationBetweenStations(Station removeStation) {
        Section upSectionOfOrigin = sections.stream()
                                            .filter(section -> section.isUpStation(removeStation))
                                            .findFirst()
                                            .orElseThrow(() -> new NotFoundException("해당 구간을 찾을 수 없습니다."));
        Section downSectionOfOrigin = sections.stream()
                                              .filter(section -> section.isDownStation(removeStation))
                                              .findFirst()
                                              .orElseThrow(() -> new NotFoundException("해당 구간을 찾을 수 없습니다."));

        int revisedDistance = upSectionOfOrigin.getDistance() + downSectionOfOrigin.getDistance();
        Section revisedSection = Section.of(upSectionOfOrigin.getId(), downSectionOfOrigin.getUpStation(), upSectionOfOrigin.getDownStation(), revisedDistance);

        sections.remove(upSectionOfOrigin);
        sections.remove(downSectionOfOrigin);
        sections.add(revisedSection);
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }

    @Override
    public String toString() {
        return "Sections{" +
                "sections=" + sections +
                '}';
    }
}

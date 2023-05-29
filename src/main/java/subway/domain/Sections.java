package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public Sections buildNewSectionsAdded(final Section newSection) {
        final List<Section> nowSections = copySections();

        if (isContainsBothStation(newSection, nowSections)) {
            throw new IllegalArgumentException("이미 존재하는 경로를 추가할 수 없습니다");
        }

        if (isContainsUpStation(newSection, nowSections)) {
            return buildSectionsBasedUp(newSection, nowSections);
        }

        if (isContainsDownStation(newSection, nowSections)) {
            return buildSectionsBasedDown(newSection, nowSections);
        }

        validateSectionsIsConnected(newSection, nowSections);

        return buildConnectedSections(newSection, nowSections);
    }

    private Sections buildConnectedSections(final Section newSection, final List<Section> nowSections) {
        List<Section> newSections = new ArrayList<>();
        Collections.addAll(newSections, nowSections.toArray(new Section[0]));
        Collections.addAll(newSections, newSection);

        return new Sections(newSections);
    }

    private Sections buildSectionsBasedDown(final Section newSection, final List<Section> nowSections) {
        final Section targetSection = nowSections.stream()
                .filter(section -> section.isSameDownStation(newSection.getDownStation()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("경로가 존재하지 않습니다"));

        validateSectionDirection(newSection, targetSection);
        final Section newUpSection = new Section(targetSection.getUpStation(), newSection.getUpStation(), targetSection.getDistance() - newSection.getDistance());

        return combineSections(newSection, nowSections, targetSection, newUpSection);
    }

    private Sections buildSectionsBasedUp(final Section newSection, final List<Section> nowSections) {
        final Section targetSection = nowSections.stream()
                .filter(section -> section.isSameUpStation(newSection.getUpStation()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다"));

        validateSectionDirection(newSection, targetSection);

        final Section newDownSection = new Section(newSection.getDownStation(), targetSection.getDownStation(), targetSection.getDistance() - newSection.getDistance());

        return combineSections(newSection, nowSections, targetSection, newDownSection);
    }

    private Sections combineSections(final Section newSection, final List<Section> nowSections, final Section targetSection, final Section newOtherSection) {
        List<Section> newSections = new ArrayList<>();
        Collections.addAll(newSections, nowSections.toArray(new Section[0]));
        Collections.addAll(newSections, newSection, newOtherSection);
        newSections.remove(targetSection);

        return new Sections(newSections);
    }

    public Sections buildNewSectionsDeleted(final Station targetStation) {
        final List<Section> nowSections = copySections();
        final List<Station> nowUpStations = nowSections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        final List<Station> nowDownStations = nowSections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        validateStationIsExists(targetStation, nowSections);

        if (copySections().size() == 1) {
            return new Sections(new ArrayList<>());
        }

        nowDownStations.removeAll(nowSections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList()));

        nowUpStations.removeAll(nowSections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList()));

        Station lastUpstation = nowUpStations.get(0);
        Station lastDownStation = nowDownStations.get(0);

        if (lastUpstation.equals(targetStation)) {
            Section targetSection = nowSections.stream()
                    .filter(section -> section.isSameUpStation(targetStation))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("경로가 존재하지 않습니다"));

            nowSections.remove(targetSection);
            return new Sections(nowSections);
        }

        if (lastDownStation.equals(targetStation)) {
            Section targetSection = nowSections.stream()
                    .filter(section -> section.isSameDownStation(targetStation))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("경로가 존재하지 않습니다"));

            nowSections.remove(targetSection);
            return new Sections(nowSections);
        }

        final Section targetUpSection = nowSections.stream()
                .filter(section -> section.isSameDownStation(targetStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("경로가 존재하지 않습니다"));

        final Section targetDownSection = nowSections.stream()
                .filter(section -> section.isSameUpStation(targetStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("경로가 존재하지 않습니다"));

        final Long newDistance = targetUpSection.getDistance() + targetDownSection.getDistance();
        final Section newSection = new Section(targetUpSection.getUpStation(), targetDownSection.getDownStation(), newDistance);

        List<Section> newSections = new ArrayList<>();
        Collections.addAll(newSections, nowSections.toArray(new Section[0]));
        newSections.remove(targetUpSection);
        newSections.remove(targetDownSection);
        newSections.add(newSection);
        return new Sections(newSections);
    }

    public List<Station> sortStations() {
        final List<Section> nowSections = copySections();
        final List<Station> nowUpStations = nowSections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());

        nowUpStations.removeAll(nowSections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList()));
        Station lastUpstation = nowUpStations.get(0);

        List<Station> stations = new ArrayList<>(List.of(lastUpstation));

        Station targetStation = lastUpstation;
        while (stations.size() < sections.size() + 1) {
            targetStation = seekNextStation(stations, targetStation);
        }
        return stations;
    }

    private Station seekNextStation(final List<Station> stations, Station targetStation) {
        for (Section section : sections) {
            if (section.getUpStation().equals(targetStation)) {
                Station nextStation = section.getDownStation();
                stations.add(nextStation);
                targetStation = nextStation;
                break;
            }
        }
        return targetStation;
    }

    private boolean isContainsStation(final Station newStation, final List<Station> nowStations) {
        return nowStations.stream()
                .anyMatch(station -> station.equals(newStation));
    }

    private boolean isContainsBothStation(final Section newSection, final List<Section> nowSections) {
        return nowSections.stream()
                .anyMatch(section -> section.isSameUpStation(newSection.getUpStation()) && section.isSameDownStation(newSection.getDownStation()));
    }

    private boolean isContainsUpStation(final Section newSection, final List<Section> nowSections) {
        return isContainsStation(newSection.getUpStation(),
                nowSections.stream()
                        .map(section -> section.getUpStation())
                        .collect(Collectors.toList()))
                &&
                !isContainsStation(newSection.getDownStation(),
                        nowSections.stream()
                                .map(section -> section.getDownStation())
                                .collect(Collectors.toList()));
    }

    private boolean isContainsDownStation(final Section newSection, final List<Section> nowSections) {
        return !isContainsStation(newSection.getUpStation(),
                nowSections.stream()
                        .map(section -> section.getUpStation())
                        .collect(Collectors.toList()))
                &&
                isContainsStation(newSection.getDownStation(),
                        nowSections.stream()
                                .map(section -> section.getDownStation())
                                .collect(Collectors.toList()));
    }

    public List<Section> findRemovedSection(Sections otherSections) {
        return this.sections.stream()
                .filter(section -> !otherSections.sections.contains(section))
                .collect(Collectors.toList());

    }

    public List<Section> findAddedSection(Sections nowSections) {
        return this.copySections().stream()
                .filter(section -> !nowSections.copySections().contains(section))
                .collect(Collectors.toList());

    }

    private void validateSectionDirection(final Section newSection, final Section targetSection) {
        if (newSection.getDistance() >= targetSection.getDistance()) {
            throw new IllegalArgumentException("새로운 경로 거리는 기존 경로보다 짧아야 합니다");
        }
    }

    private void validateSectionsIsConnected(final Section newSection, final List<Section> nowSections) {
        if (nowSections.size() > 0) {
            if (
                    !isContainsStation(newSection.getUpStation(), nowSections.stream()
                            .map(section -> section.getDownStation())
                            .collect(Collectors.toList()))
                            &&
                            !isContainsStation(newSection.getDownStation(), nowSections.stream()
                                    .map(section -> section.getUpStation())
                                    .collect(Collectors.toList()))
            ) {
                throw new IllegalArgumentException("이어지지 않는 경로를 추가할 수 없습니다");
            }
        }
    }

    private void validateStationIsExists(final Station targetStation, final List<Section> sections) {
        sections.stream()
                .filter(section -> section.isSameUpStation(targetStation) || section.isSameDownStation(targetStation))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("이미 존재하는 역만 삭제할 수 있습니다"));
    }

    public List<Section> copySections() {
        return new ArrayList<>(sections);
    }
}

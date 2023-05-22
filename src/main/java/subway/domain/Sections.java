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

    public Sections addSection(final Section newSection) {
        final List<Section> nowSections = copySections();
        final Station newUpStation = newSection.getUpStation();
        final Station newDownStation = newSection.getDownStation();
        final List<Station> nowUpStations = nowSections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        final List<Station> nowDownStations = nowSections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        if (isContainsStation(newUpStation, nowUpStations) && isContainsStation(newDownStation, nowDownStations)) {
            throw new IllegalArgumentException("이미 존재하는 경로를 추가할 수 없습니다");
        }

        if (isContainsStation(newUpStation, nowUpStations) && !isContainsStation(newDownStation, nowDownStations)) {
            final Station targetUpStation = nowUpStations.stream()
                    .filter(station -> station.equals(newUpStation))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다"));

            final Section targetSection = nowSections.stream()
                    .filter(section -> section.isSameUpStation(targetUpStation))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("경로가 존재하지 않습니다"));

            validateSectionDirection(newSection, targetSection);

            final Section newDownSection = new Section(newSection.getDownStation(), targetSection.getDownStation(), targetSection.getDistance() - newSection.getDistance());

            List<Section> newSections = new ArrayList<>();
            Collections.addAll(newSections, nowSections.toArray(new Section[0]));
            Collections.addAll(newSections, newSection, newDownSection);
            newSections.remove(targetSection);

            return new Sections(newSections);
        }

        if ((!isContainsStation(newUpStation, nowUpStations)) && isContainsStation(newDownStation, nowDownStations)) {
            final Station targetDownStation = nowDownStations.stream()
                    .filter(station -> station.equals(newDownStation))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다"));

            final Section targetSection = nowSections.stream()
                    .filter(section -> section.isSameDownStation(targetDownStation))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("경로가 존재하지 않습니다"));

            validateSectionDirection(newSection, targetSection);
            final Section newUpSection = new Section(targetSection.getUpStation(), newSection.getUpStation(), targetSection.getDistance() - newSection.getDistance());

            List<Section> newSections = new ArrayList<>();
            Collections.addAll(newSections, nowSections.toArray(new Section[0]));
            Collections.addAll(newSections, newSection, newUpSection);
            newSections.remove(targetSection);
            return new Sections(newSections);
        }

        validateSectionsIsConnected(newUpStation, newDownStation, nowUpStations, nowDownStations);

        List<Section> newSections = new ArrayList<>();
        Collections.addAll(newSections, nowSections.toArray(new Section[0]));
        Collections.addAll(newSections, newSection);

        return new Sections(newSections);
    }

    public Sections deleteSection(final Station targetStation) {
        final List<Section> nowSections = copySections();
        final List<Station> nowUpStations = nowSections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        final List<Station> nowDownStations = nowSections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        validateStationIsExists(targetStation, nowUpStations, nowDownStations);

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

    private void validateSectionsIsConnected(final Station newUpStation, final Station newDownStation, final List<Station> nowUpStations, final List<Station> nowDownStations) {
        if (sections.size() > 0) {
            if ((!isContainsStation(newUpStation, nowDownStations)) && !isContainsStation(newDownStation, nowUpStations)) {
                throw new IllegalArgumentException("이어지지 않는 경로를 추가할 수 없습니다");
            }
        }
    }

    private void validateStationIsExists(final Station targetStation, final List<Station> nowUpStations, final List<Station> nowDownStations) {
        if ((!isContainsStation(targetStation, nowUpStations)) && !isContainsStation(targetStation, nowDownStations)) {
            throw new IllegalArgumentException("이미 존재하는 역만 삭제할 수 있습니다");
        }
    }

    public List<Section> copySections() {
        return new ArrayList<>(sections);
    }
}

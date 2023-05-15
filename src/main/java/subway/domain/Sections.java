package subway.domain;

import subway.exception.AddStationException;
import subway.exception.RemoveStationException;

import java.util.List;
import java.util.Objects;

public class Sections {
    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public Section add(Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return null;
        }

        Station preStation = newSection.getPreStation();
        Station station = newSection.getStation();

        if (isExistStation(preStation) && !isExistStation(station)) {
            if (isDownEndStation(preStation)) {
                sections.add(newSection);
                return null;
            }

            Section original = sections.stream()
                    .filter(section -> Objects.equals(section.getPreStation(), preStation))
                    .findFirst().orElseThrow(() -> new AddStationException("다음 역을 찾을 수 없습니다"));
            if (original.getDistance() <= newSection.getDistance()) {
                throw new AddStationException("기존 구간의 길이보다 큰 길이의 구간은 해당 구간 사이에 추가할 수 없습니다.");
            }
            sections.add(newSection);
            return new Section(newSection.getLine(), newSection.getStation(),
                    original.getStation(), original.getDistance() - newSection.getDistance());
        }

        if (!isExistStation(preStation) && isExistStation(station)) {
            if (isUpEndStation(station)) {
                sections.add(newSection);
                return null;
            }

            Section original = sections.stream()
                    .filter(section -> Objects.equals(section.getStation(), station))
                    .findFirst().orElseThrow(() -> new AddStationException("이전 역을 찾을 수 없습니다"));
            if (original.getDistance() <= newSection.getDistance()) {
                throw new AddStationException("기존 구간의 길이보다 큰 길이의 구간은 해당 구간 사이에 추가할 수 없습니다.");
            }
            sections.add(newSection);
            return new Section(newSection.getLine(), original.getPreStation(),
                    newSection.getPreStation(), original.getDistance() - newSection.getDistance());
        }

        throw new AddStationException("추가할 수 없는 구간입니다");
    }

    public List<Section> getSections() {
        return sections;
    }

    //    public List<PairId> getPairStationIds() {
//        return sections.stream().map(section -> new PairId(section.getPreStationId(), section.getStationId()))
//                .collect(Collectors.toList());
//    }
//
    private boolean isExistStation(Station station) {
        return sections.stream()
                .anyMatch(section -> Objects.equals(section.getStation(), station)
                        || Objects.equals(section.getPreStation(), station));
    }

    private boolean isUpEndStation(Station station) {
        return sections.stream().noneMatch(section -> Objects.equals(section.getStation(), station));
    }

    private boolean isDownEndStation(Station station) {
        return sections.stream().noneMatch(section -> Objects.equals(section.getPreStation(), station));
    }


    public void removeWhenOnlyTwoStationsExist(Station station) {
        if (isExistStation(station)) {
            sections.clear();
            return;
        }
        throw new RemoveStationException("구간 내에 포함되어 있지 않은 역은 삭제할 수 없습니다");
    }


    public Section remove(Station station) {
        if (!isExistStation(station)) {
            throw new RemoveStationException("구간 내에 포함되어 있지 않은 역은 삭제할 수 없습니다");
        }

        if (isUpEndStation(station) || isDownEndStation(station)) {
            sections.removeIf(section -> Objects.equals(section.getPreStation(), station) ||
                    Objects.equals(section.getStation(), station));
            return null;
        }

        Section preSection = sections.stream()
                .filter(section -> Objects.equals(section.getStation(), station))
                .findFirst().orElseThrow(() -> new RemoveStationException("해당 역의 구간을 찾을 수 없습니다"));
        Section postSection = sections.stream()
                .filter(section -> Objects.equals(section.getPreStation(), station))
                .findFirst().orElseThrow(() -> new RemoveStationException("해당 역의 구간을 찾을 수 없습니다"));

        sections.remove(preSection);
        sections.remove(postSection);

        Section newSection = new Section(preSection.getLine(), preSection.getPreStation(),
                postSection.getStation(), preSection.getDistance() + postSection.getDistance());
        sections.add(newSection);
        return newSection;
    }
}

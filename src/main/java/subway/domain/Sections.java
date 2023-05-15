package subway.domain;

import subway.exception.AddStationException;

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


//    public void removeWhenOnlyTwoStationsExist(Long stationId) {
//        sections.clear();
//    }
//
//    public Section remove(Long stationId) {
//        Optional<Section> preSection = sections.stream()
//                .filter(section -> section.getStationId() == stationId).findFirst();
//        Optional<Section> postSection = sections.stream()
//                .filter(section -> section.getPreStationId() == stationId).findFirst();
//
//
//        // 상행 종점 지울 때
//        if (preSection.isEmpty() && postSection.isPresent()) {
//            return null;
//        }
//
//        // 하행 종점 지울 때
//        if (postSection.isEmpty() && preSection.isPresent()) {
//            return null;
//        }
//
//        Long preStationId = preSection.get().getPreStationId();
//        Long postStationId = postSection.get().getStationId();
//        Long distance = preSection.get().getDistance() + postSection.get().getDistance();
//        Long lineId = preSection.get().getLineId();
//
//        return new Section(lineId, preStationId, postStationId, distance);
//    }
}

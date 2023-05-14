package subway.domain;

import subway.exception.AddStationException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Section modified = null;
        for (Section section : sections) {
            if (Objects.equals(section.getPreStationId(), newSection.getPreStationId())) {
                if (section.getDistance() <= newSection.getDistance()) {
                    throw new AddStationException("새로 추가되는 역과 기존 역 사이의 거리가 기존 구간의 거리 이상입니다");
                }
                modified = section;
                modified.updatePreStation(newSection.getStationId());
                break;
            }
        }

        sections.add(newSection);
        return modified;
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<PairId> getPairStationIds() {
        return sections.stream().map(section -> new PairId(section.getPreStationId(), section.getStationId()))
                .collect(Collectors.toList());
    }

    public boolean isExistStation(Long stationId) {
        return sections.stream()
                .anyMatch(section -> Objects.equals(section.getStationId(), stationId)
                        || Objects.equals(section.getPreStationId(), stationId));
    }

    public void removeWhenOnlyTwoStationsExist(Long stationId) {
        sections.clear();
    }

    public Section remove(Long stationId) {
        Optional<Section> preSection = sections.stream()
                .filter(section -> section.getStationId() == stationId).findFirst();
        Optional<Section> postSection = sections.stream()
                .filter(section -> section.getPreStationId() == stationId).findFirst();


        // 상행 종점 지울 때
        if (preSection.isEmpty() && postSection.isPresent()) {
            return null;
        }

        // 하행 종점 지울 때
        if (postSection.isEmpty() && preSection.isPresent()) {
            return null;
        }

        Long preStationId = preSection.get().getPreStationId();
        Long postStationId = postSection.get().getStationId();
        Long distance = preSection.get().getDistance() + postSection.get().getDistance();
        Long lineId = preSection.get().getLineId();

        return new Section(lineId, preStationId, postStationId, distance);
    }
}

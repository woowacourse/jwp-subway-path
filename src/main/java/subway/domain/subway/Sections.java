package subway.domain.subway;

import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sections {

    private static final int INITIAL_SIZE = 1;
    private final List<Section> sections;

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections from(List<Section> unsortedSections) {
        return new Sections(sortSections(unsortedSections));
    }

    private static List<Section> sortSections(List<Section> sections) {
        final Long firstStationId = getFirstStations(sections);
        List<Section> result = new ArrayList<>();
        Long next = firstStationId;
        for (int i = 0; i < sections.size(); i++) {
            next = addNextSection(sections, result, next);
        }
        return result;
    }

    private static Long getFirstStations(List<Section> sections) {
        if (sections.isEmpty()) {
            throw new IllegalStateException("빈 라인은 첫번째 역을 찾을 수  없습니다.");
        }
        final Set<Long> allStationIds = sections.stream()
                .flatMap(section -> Stream.of(section.getUpStationId(), section.getDownStationId()))
                .collect(Collectors.toSet());

        final Set<Long> downStationIds = sections.stream()
                .map(Section::getDownStationId)
                .collect(Collectors.toSet());

        final List<Long> stationIds = new ArrayList<>(allStationIds);
        stationIds.removeAll(downStationIds);

        return stationIds.get(0);
    }

    private static Long addNextSection(List<Section> sections, List<Section> result, Long nextStationId) {
        for (Section section : sections) {
            if (section.getUpStationId().equals(nextStationId)) {
                nextStationId = section.getDownStationId();
                result.add(section);
                break;
            }
        }
        return nextStationId;
    }

    public boolean isDownEndPoint(Long upStationId) {
        return Objects.equals(upStationId, sections.get(sections.size() - 1).getDownStationId());
    }

    public boolean isUpEndPoint(Long downStationId) {
        return Objects.equals(downStationId, sections.get(0).getUpStationId());
    }

    public boolean isUpStationPoint(Long upStationId) {
        return sections.stream()
                .anyMatch(section -> Objects.equals(section.getUpStationId(), upStationId));
    }

    public Section getTargetUpStationSection(Long upStationId) {
        return sections.stream()
                .filter(section -> Objects.equals(section.getUpStationId(), upStationId))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("찾을 수 없는 구간입니다."));
    }

    public Section getTargetDownStationSection(Long downStationId) {
        return sections.stream()
                .filter(section -> Objects.equals(section.getDownStationId(), downStationId))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("찾을 수 없는 구간입니다."));
    }

    public List<Section> findIncludeTargetSection(Long stationId) {
        return sections.stream()
                .filter(section -> Objects.equals(section.getUpStationId(), stationId) || Objects.equals(section.getDownStationId(), stationId))
                .collect(Collectors.toList());
    }

    public void deleteSection(SectionDeleteRequest request) {
        if (isInitialState()) {
            sections.clear();
            return;
        }

        if (isDownEndPoint(request.getStationId())) {
            sections.remove(sections.size() - 1);
            return;
        }

        if (isUpEndPoint(request.getStationId())) {
            sections.remove(0);
            return;
        }

        final List<Section> includeTargetSection = findIncludeTargetSection(request.getStationId());
        final int newDistance = includeTargetSection.stream()
                .mapToInt(Section::getDistance)
                .sum();

        final Section forwardSection = includeTargetSection.get(0);
        final Section backwardSection = includeTargetSection.get(1);

        sections.add(new Section(new Distance(newDistance),
                new Station(forwardSection.getUpStationId(), forwardSection.getUpStation().getName()),
                new Station(backwardSection.getDownStationId(), backwardSection.getDownStation().getName()),
                request.getLineId()));
        for (Section section : includeTargetSection) {
            sections.remove(section);
        }
    }

    public List<Station> getSortedStations() {
        final List<Station> stations = sections.stream()
                .map(section -> new Station(section.getUpStationId(), section.getUpStation().getName()))
                .collect(Collectors.toList());

        final Section lastSection = sections.get(sections.size() - 1);
        stations.add(new Station(lastSection.getDownStationId(), lastSection.getDownStation().getName()));

        return stations;
    }

    public void isExistSection(SectionRequest request) {
        for (Section section : sections) {
            if (Objects.equals(section.getUpStationId(), request.getUpStationId()) && Objects.equals(section.getDownStationId(), request.getDownStationId())
                    || Objects.equals(section.getDownStationId(), request.getUpStationId()) && Objects.equals(section.getUpStationId(), request.getDownStationId())) {
                throw new IllegalArgumentException("동일한 구간을 추가할 수 없습니다.");
            }
        }
    }

    public boolean isInitialState() {
        return sections.size() == INITIAL_SIZE;
    }

    public List<Section> getSections() {
        return sections;
    }
}

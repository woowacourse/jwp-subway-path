package subway.domain;

import java.util.*;
import java.util.stream.Collectors;

public class SectionMap {

    public static final String SECTION_MAP_EMPTY_EXCEPTION = "해당 노선은 비어있습니다.";
    public static final String STATION_NOT_EXIST_IN_LINE_EXCEPTION = "해당 역이 노선에 존재하지 않습니다.";
    public static final String STATION_ALREADY_EXIST_IN_LINE_EXCEPTION = "해당 노선에 이미 존재하는 역입니다.";
    private final Map<Station, Section> sectionMap;

    public SectionMap() {
        this.sectionMap = new LinkedHashMap<>();
    }

    public SectionMap(final Map<Station, Section> sectionMap, final Station upEndstation) {
        if (sectionMap.isEmpty()) {
            this.sectionMap = new LinkedHashMap<>();
            return;
        }

        this.sectionMap = sortSectionMap(sectionMap, upEndstation);
    }

    private LinkedHashMap<Station, Section> sortSectionMap(final Map<Station, Section> sectionMap, final Station upEndstation) {
        final LinkedHashMap<Station, Section> sortedSectionMap = new LinkedHashMap<>();
        Station current = upEndstation;

        while (!sectionMap.isEmpty()) {
            final Station next = sectionMap.get(current).getDownStation();
            sortedSectionMap.put(current, sectionMap.remove(current));
            current = next;
        }

        return sortedSectionMap;
    }

    public static SectionMap generateBySections(final List<Section> sections, final Station upEndstation) {
        final Map<Station, Section> sectionMap = new HashMap<>();
        sections.forEach(section -> sectionMap.put(section.getUpStation(), section));

        return new SectionMap(sectionMap, upEndstation);
    }

    public Section addInitialSection(final Station upStation, final Station downStation, final int distance) {
        if (!sectionMap.isEmpty()) {
            throw new IllegalArgumentException("첫 역 추가 오류: 해당 노선은 비어있지 않습니다.");
        }
        return addEndSection(upStation, downStation, distance);
    }

    private Section addEndSection(final Station upStation, final Station downStation, final int distance) {
        final Section section = new Section(upStation, downStation, distance);
        sectionMap.put(upStation, section);
        return section;
    }

    public Section addSection(final Station upStation, final Station downStation, final int distance) {
        final boolean isUpStationExist = isStationExist(upStation);
        final boolean isDownStationExist = isStationExist(downStation);

        if (isUpStationExist && isDownStationExist) {
            throw new IllegalArgumentException(STATION_ALREADY_EXIST_IN_LINE_EXCEPTION);
        }
        if (upStation.equals(findLastStation())) {
            return addEndSection(upStation, downStation, distance);
        }
        if (downStation.equals(findFirstStation())) {
            return addEndSection(upStation, downStation, distance);
        }
        if (isUpStationExist) {
            return addMiddleSectionBasedUpStation(upStation, downStation, distance);
        }
        if (isDownStationExist) {
            return addMiddleSectionBasedDownStation(upStation, downStation, distance);
        }
        throw new IllegalArgumentException(STATION_NOT_EXIST_IN_LINE_EXCEPTION);
    }

    private boolean isStationExist(final Station station) {
        return sectionMap.containsKey(station) || findLastStation().equals(station);
    }

    private Station findLastStation() {
        final Optional<Station> lastKey = sectionMap.keySet().stream()
                .reduce((first, second) -> second);
        if (lastKey.isEmpty()) {
            throw new IllegalArgumentException(SECTION_MAP_EMPTY_EXCEPTION);
        }
        return sectionMap.get(lastKey.get()).getDownStation();
    }

    private Station findFirstStation() {
        return sectionMap.keySet().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(SECTION_MAP_EMPTY_EXCEPTION));
    }

    private Section addMiddleSectionBasedUpStation(final Station upStation, final Station downStation, final int distance) {
        final Section oldSection = sectionMap.get(upStation);
        validateDistance(distance, oldSection.getDistance());

        final Section newUpSection = new Section(upStation, downStation, distance);
        sectionMap.replace(upStation, newUpSection);

        final Section newDownSection = new Section(downStation, oldSection.getDownStation(), oldSection.getDistance() - distance);
        sectionMap.put(downStation, newDownSection);

        return newUpSection;
    }

    private Section addMiddleSectionBasedDownStation(final Station upStation, final Station downStation, final int distance) {
        final Station previousStation = findPreviousStation(downStation);
        final Section oldSection = sectionMap.get(previousStation);
        validateDistance(distance, oldSection.getDistance());

        final Section newUpSection = new Section(oldSection.getUpStation(), upStation, oldSection.getDistance() - distance);
        sectionMap.replace(oldSection.getUpStation(), newUpSection);

        final Section newDownSection = new Section(upStation, downStation, distance);
        sectionMap.put(upStation, newDownSection);

        return newDownSection;
    }

    private void validateDistance(final int distance, final int oldDistance) {
        if (distance >= oldDistance) {
            throw new IllegalArgumentException("역 추가 오류 : 추가할 수 없는 거리입니다.");
        }
    }

    private Station findPreviousStation(final Station station) {
        return sectionMap.keySet().stream()
                .filter(s -> sectionMap.get(s).getDownStation().equals(station))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("이전 역이 존재하지 않습니다."));
    }

    public void deleteStation(final Station station) {
        if (!isStationExist(station)) {
            throw new IllegalArgumentException(STATION_NOT_EXIST_IN_LINE_EXCEPTION);
        }
        if (sectionMap.size() == 1) {
            sectionMap.clear();
            return;
        }
        if (station.equals(findFirstStation())) {
            sectionMap.remove(findFirstStation());
            return;
        }
        if (station.equals(findLastStation())) {
            sectionMap.remove(findPreviousStation(station));
            return;
        }
        deleteMiddleStation(station);
    }

    private void deleteMiddleStation(final Station station) {
        final Station previousStation = findPreviousStation(station);
        final Section oldUpSection = sectionMap.get(previousStation);
        final Section oldDownSection = sectionMap.get(station);

        final int newDistance = oldUpSection.getDistance() + oldDownSection.getDistance();
        final Section newSection = new Section(oldUpSection.getUpStation(), oldDownSection.getDownStation(), newDistance);

        sectionMap.remove(station);
        sectionMap.replace(previousStation, newSection);
    }

    public Optional<Station> getUpEndpoint() {
        final List<Station> downStations = sectionMap.values().stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        return sectionMap.keySet().stream()
                .filter(station -> !downStations.contains(station))
                .findFirst();
    }

    public List<Station> getAllStations() {
        final List<Station> stations = new ArrayList<>(sectionMap.keySet());
        stations.add(findLastStation());
        return Collections.unmodifiableList(stations);
    }

    public Map<Station, Section> getSectionMap() {
        return new LinkedHashMap<>(sectionMap);
    }
}

package subway.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class SectionMap {

    public static final String SECTIONMAP_EMPTY_EXCEPTION = "노선이 비어있습니다.";
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

    public void addInitialSection(final Station upStation, final Station downStation, final int distance) {
        if (!sectionMap.isEmpty()) {
            throw new IllegalArgumentException("첫 역 추가 오류: 해당 노선은 비어있지 않습니다.");
        }

        final Section section = new Section(upStation, downStation, distance);
        sectionMap.put(upStation, section);
    }

    public Section addSection(final Station upStation, final Station downStation, final int distance) {
        final boolean isUpStationExist = isStationExist(upStation);
        final boolean isDownStationExist = isStationExist(downStation);

        if (isUpStationExist && isDownStationExist) {
            throw new IllegalArgumentException("이미 노선에 존재하는 역입니다.");
        }
        if (isUpStationExist) {
            return addSectionBasedUpStation(upStation, downStation, distance);
        }
        if (isDownStationExist) {
            return addSectionBasedDownStation(upStation, downStation, distance);
        }
        throw new IllegalArgumentException("해당 역이 노선에 존재하지 않습니다.");
    }

    private boolean isStationExist(final Station station) {
        return sectionMap.containsKey(station) || findDownEndstation().equals(station);
    }

    private Section addSectionBasedUpStation(final Station upStation, final Station downStation, final int distance) {
        if (upStation.equals(findDownEndstation())) {
            final Section section = new Section(upStation, downStation, distance);
            sectionMap.put(upStation, section);
            return section;
        }

        final Section oldSection = sectionMap.get(upStation);
        validateDistance(distance, oldSection.getDistance());

        final Section newUpSection = new Section(upStation, downStation, distance);
        sectionMap.replace(upStation, newUpSection);

        final Section newDownSection = new Section(downStation, oldSection.getDownStation(), oldSection.getDistance() - distance);
        sectionMap.put(downStation, newDownSection);

        return newUpSection;
    }

    private Section addSectionBasedDownStation(final Station upStation, final Station downStation, final int distance) {
        if (downStation.equals(findUpEndstation())) {
            final Section section = new Section(upStation, downStation, distance);
            sectionMap.put(upStation, section);
            return section;
        }

        final Station previousStation = findPreviousStation(downStation)
                .orElseThrow(() -> new IllegalArgumentException("이전 역이 존재하지 않습니다."));
        final Section oldSection = sectionMap.get(previousStation);
        validateDistance(distance, oldSection.getDistance());

        final Section newUpSection = new Section(oldSection.getUpStation(), upStation, oldSection.getDistance() - distance);
        sectionMap.replace(oldSection.getUpStation(), newUpSection);

        final Section newDownSection = new Section(upStation, downStation, distance);
        sectionMap.put(upStation, newDownSection);

        return newDownSection;
    }

    private Station findDownEndstation() {
        final Optional<Station> lastKey = sectionMap.keySet().stream()
                .reduce((first, second) -> second);
        if (lastKey.isEmpty()) {
            throw new IllegalArgumentException(SECTIONMAP_EMPTY_EXCEPTION);
        }
        return sectionMap.get(lastKey.get()).getDownStation();
    }

    private void validateDistance(final int distance, final int oldDistance) {
        if (distance >= oldDistance) {
            throw new IllegalArgumentException("역 추가 오류 : 추가할 수 없는 거리입니다.");
        }
    }

    private Station findUpEndstation() {
        return sectionMap.keySet().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(SECTIONMAP_EMPTY_EXCEPTION));
    }

    private Optional<Station> findPreviousStation(final Station station) {
        return sectionMap.keySet().stream()
                .filter(s -> sectionMap.get(s).getDownStation().equals(station))
                .findFirst();
    }

    public Map<Station, Section> getSectionMap() {
        return new LinkedHashMap<>(sectionMap);
    }
}

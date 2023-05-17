package subway.domain.section;

import java.util.List;
import java.util.Map;
import subway.domain.station.Station;

public class SectionRemover {

    private static final int MINIMUM_SECTION_SIZE = 2;
    private static final String INVALID_REMOVE_MIDDLE_STATION = "구간에서 삭제할 올바른 역을 선택해주세요.";

    private final Map<Station, Section> sections;

    private SectionRemover(final Map<Station, Section> sections) {
        this.sections = sections;
    }

    public static SectionRemover from(final Map<Station, Section> sections) {
        return new SectionRemover(sections);
    }

    public Map<Station, Section> removeStation(final Station targetStation) {
        validateRemoveStation(targetStation);

        if (sections.values().size() == MINIMUM_SECTION_SIZE) {
            sections.clear();
            return sections;
        }

        final Section section = sections.get(targetStation);

        if (section.isTerminalStation()) {
            return removeSectionToTerminalStation(targetStation);
        }
        return removeSectionToMiddleStation(targetStation);
    }

    private void validateRemoveStation(final Station targetStation) {
        validateSection();
        validateRemoveTargetStation(targetStation);
    }

    private void validateRemoveTargetStation(final Station targetStation) {
        if (!isRegisterStation(targetStation)) {
            throw new IllegalArgumentException("해당 역은 노선에 등록되어 있지 않습니다.");
        }
    }

    private boolean isRegisterStation(final Station targetStation) {
        return sections.containsKey(targetStation);
    }

    private void validateSection() {
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("해당 역은 구간이 존재하지 않습니다.");
        }
    }

    private Map<Station, Section> removeSectionToMiddleStation(final Station targetStation) {
        final Section section = sections.get(targetStation);
        final Station upStation = section.findStationByDirection(Direction.UP)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_REMOVE_MIDDLE_STATION));
        final Station downStation = section.findStationByDirection(Direction.DOWN)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_REMOVE_MIDDLE_STATION));

        removeSectionInfo(targetStation, upStation, downStation);
        sections.remove(targetStation);

        return sections;
    }

    private void removeSectionInfo(final Station targetStation, final Station upStation, final Station downStation) {
        final Section section = sections.get(targetStation);
        final Distance upStationDistance = section.findDistanceByStation(upStation);
        final Distance downStationDistance = section.findDistanceByStation(downStation);
        final Distance distance = upStationDistance.plus(downStationDistance);

        final Section upStationSection = sections.get(upStation);
        upStationSection.delete(targetStation);
        upStationSection.add(downStation, distance, Direction.DOWN);

        final Section downStationSection = sections.get(downStation);
        downStationSection.delete(targetStation);
        downStationSection.add(upStation, distance, Direction.UP);
    }

    private Map<Station, Section> removeSectionToTerminalStation(final Station targetStation) {
        final Section section = sections.get(targetStation);
        final List<Station> stations = section.findAllStation();

        for (Station station : stations) {
            validateRegisterStation(station);
            sections.get(station)
                    .delete(targetStation);
        }

        sections.remove(targetStation);
        return sections;
    }

    private void validateRegisterStation(final Station station) {
        if (!sections.containsKey(station)) {
            throw new IllegalArgumentException("해당 역은 해당 노선에 등록되지 않은 역입니다.");
        }
    }
}

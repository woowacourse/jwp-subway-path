package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import subway.exception.GlobalException;

public class Sections {
    private final List<Section> sections;
    private Station upStation;
    private Station downStation;

    public Sections(final List<Section> sections, final Station upStation, final Station downStation) {
        validateDuplication(sections);
        this.sections = new ArrayList<>(sections);
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public void add(Section newSection) {
        if (sections.contains(newSection)) {
            throw new GlobalException("이미 존재하는 구간입니다.");
        }

        validateConnection(newSection);

        if (newSection.isSameEndStation(upStation)) {
            sections.add(newSection);
            upStation = newSection.getStartStation();
            return;
        }

        if (newSection.isSameStartStation(downStation)) {
            sections.add(newSection);
            downStation = newSection.getEndStation();
            return;
        }

        Section findSection = findForAddByDistance(newSection);

        if (findSection.isSameStartStation(newSection)) {
            Distance subtractedDistance = findSection.subtractDistance(newSection);
            Section devidedSection = new Section(newSection.getEndStation(), findSection.getEndStation(),
                    subtractedDistance);

            sections.remove(findSection);
            sections.add(newSection);
            sections.add(devidedSection);
            return;
        }

        Distance subtractedDistance = findSection.subtractDistance(newSection);
        Section devidedSection = new Section(findSection.getStartStation(), newSection.getStartStation(),
                subtractedDistance);

        sections.remove(findSection);
        sections.add(newSection);
        sections.add(devidedSection);
    }

    private Section findForAddByDistance(final Section newSection) {
        Optional<Section> findSameStartStationSection = sections.stream()
                .filter(newSection::isSameStartStation)
                .findFirst();

        if (findSameStartStationSection.isPresent()) {
            Section findSection = findSameStartStationSection.get();
            if (findSection.isGreaterThanOtherDistance(newSection)) {
                return findSection;
            }
        }

        Optional<Section> findSameEndStationSection = sections.stream()
                .filter(newSection::isSameEndStation)
                .findFirst();

        if (findSameEndStationSection.isPresent()) {
            Section findSection = findSameEndStationSection.get();
            if (findSection.isGreaterThanOtherDistance(newSection)) {
                return findSection;
            }
        }
        throw new GlobalException("구간 길이로 인해 연결할 수 없습니다.");
    }

    private void validateDuplication(final List<Section> sections) {
        long distinctSize = sections.stream()
                .distinct()
                .count();

        if (sections.isEmpty() || sections.size() != distinctSize) {
            throw new GlobalException("구간은 중복될 수 없습니다.");
        }
    }

    private void validateConnection(final Section section) {
        boolean isPresentSameStartStation = sections.stream()
                .anyMatch(it -> it.isSameStartStation(section.getStartStation()) ||
                        it.isSameStartStation(section.getEndStation()));

        boolean isPresentSameEndStation = sections.stream()
                .anyMatch(it -> it.isSameEndStation(section.getStartStation()) ||
                        it.isSameEndStation(section.getEndStation()));

        if (isPresentSameStartStation && isPresentSameEndStation) {
            throw new GlobalException("이미 연결되어 있는 구간입니다.");
        }

        if (!isPresentSameStartStation && !isPresentSameEndStation) {
            throw new GlobalException("연결되어 있지 않은 구간입니다.");
        }
    }
}

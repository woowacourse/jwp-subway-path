package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Line {
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 10;

    private final String name;
    private final String color;
    private final List<Section> sections;

    public Line(final String name, final String color, final List<Section> sections) {
        validate(name);
        this.name = name;
        this.color = color;
        this.sections = new ArrayList<>(sections);
    }

    private void validate(final String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(String.format("노선 이름은 %d~%d자 사이여야 합니다.", MIN_NAME_LENGTH, MAX_NAME_LENGTH));
        }
    }

    public void register(final Station source, final Station target, final int distance) {
        if (sections.isEmpty()) {
            sections.add(new Section(source, target, distance));
            return;
        }
        validateRegister(source, target);
        final Station existence = getExistingStation(source, target);
        if (existence.equals(source)) {
            registerTargetStation(existence, target, distance);
        }
        if (existence.equals(target)) {
            registerSourceStation(existence, source, distance);
        }
    }

    private void validateRegister(final Station source, final Station target) {
        if (doesNotHave(source) && doesNotHave(target)) {
            throw new IllegalArgumentException("기준역이 존재하지 않아 추가할 수 없습니다.");
        }
        if (have(source) && have(target)) {
            throw new IllegalArgumentException("두 역 모두 노선에 존재하는 역입니다.");
        }
    }

    private boolean doesNotHave(final Station station) {
        return !have(station);
    }

    private boolean have(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.have(station));
    }

    private Station getExistingStation(final Station source, final Station target) {
        if (have(source)) {
            return source;
        }
        return target;
    }

    private void registerTargetStation(final Station existence, final Station additional, final int distance) {
        if (isTargetDistanceUnRegistrable(existence, distance)) {
            throw new IllegalArgumentException("등록하려는 구간의 거리는 기존 구간의 거리보다 짧아야 합니다.");
        }
        final Optional<Section> foundSection = findSourceSection(existence);
        if (foundSection.isPresent()) {
            changeDistance(additional, foundSection.get().getTarget(), foundSection.get(), distance);
        }
        sections.add(new Section(existence, additional, distance));
    }

    private boolean isTargetDistanceUnRegistrable(final Station existence, final int distance) {
        final Optional<Section> foundSourceSection = findSourceSection(existence);
        return foundSourceSection.map(section -> section.isLongOrEqualThan(distance))
                .orElse(false);
    }

    private Optional<Section> findSourceSection(final Station existence) {
        return sections.stream()
                .filter(section -> section.isSource(existence))
                .findAny();
    }

    private void changeDistance(final Station source, final Station target, final Section oldSection, final int distance) {
        sections.add(new Section(source, target, oldSection.getDistance() - distance));
        sections.remove(oldSection);
    }

    private void registerSourceStation(final Station existence, final Station additional, final int distance) {
        if (isSourceDistanceUnRegistrable(existence, distance)) {
            throw new IllegalArgumentException("등록하려는 구간의 거리는 기존 구간의 거리보다 짧아야 합니다.");
        }
        final Optional<Section> foundSection = findTargetSection(existence);
        if (foundSection.isPresent()) {
            changeDistance(foundSection.get().getSource(), additional, foundSection.get(), distance);
        }
        sections.add(new Section(additional, existence, distance));
    }

    private boolean isSourceDistanceUnRegistrable(final Station existence, final int distance) {
        final Optional<Section> foundSection = findSourceSection(existence);
        return foundSection.map(section -> section.isLongOrEqualThan(distance))
                .orElse(false);
    }

    private Optional<Section> findTargetSection(final Station existence) {
        return sections.stream()
                .filter(section -> section.isTarget(existence))
                .findAny();
    }

    public void delete(final Station station) {
        final Optional<Section> foundSourceSection = findSourceSection(station);
        final Optional<Section> foundTargetSection = findTargetSection(station);
        if (foundSourceSection.isPresent() && foundTargetSection.isPresent()) {
            mergeSections(foundTargetSection, foundSourceSection);
            return;
        }
        deleteLastStation(foundTargetSection, foundSourceSection);
    }

    private void mergeSections(final Optional<Section> foundTargetSection, final Optional<Section> foundSourceSection) {
        final int newDistance = foundTargetSection.get().getDistance() + foundSourceSection.get().getDistance();
        sections.add(new Section(foundTargetSection.get().getSource(), foundSourceSection.get().getTarget(), newDistance));
        sections.remove(foundTargetSection.get());
        sections.remove(foundSourceSection.get());
    }

    private void deleteLastStation(final Optional<Section> upSection, final Optional<Section> downSection) {
        if (downSection.isPresent()) {
            sections.remove(downSection.get());
            return;
        }
        if (upSection.isPresent()) {
            sections.remove(upSection.get());
            return;
        }
    }

    public List<Section> getSections() {
        return sections;
    }
}

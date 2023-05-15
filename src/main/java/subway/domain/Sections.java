package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections() {
        this.sections = new ArrayList<>();
    }

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void insertInitially(final Line line, final Station from, final Station to, final int distance) {
        sections.add(new Section(line.getId(), from, to, distance));
    }

    public void insert(final Line line, final Station from, final Station to, final int distance) {
        // 역 존재
        if (exist(from) == exist(to)) {
            throw new IllegalArgumentException("해당 조건으로 역을 설치할 수 없습니다.");
        }

        if (exist(from)) {
            // 추가 - 사이 오른쪽에 넣기
            if (!isRightEnd(from)) {
                final Section changedSection = sections.stream()
                        .filter(section -> section.existLeft(from) && section.isInsertable(distance))
                        .map(section -> section.changeLeft(to, distance))
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("삽입할 수 없는 거리입니다."));

                final Section pastSection = sections.stream()
                        .filter(section -> section.existLeft(from))
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("삽입할 수 없는 거리입니다."));

                sections.remove(pastSection);
                sections.add(changedSection);
            }
            //추가 - 오른쪽 끝에 넣기
            sections.add(new Section(line.getId(), from, to, distance));
            return;
        }

        // 추가 - 사이 왼쪽에 넣기
        if (exist(to)) {
            if (!isLeftEnd(to)) {
                final Section changedSection2 = sections.stream()
                        .filter(section -> section.existRight(to) && section.isInsertable(distance))
                        .map(section -> section.changeRight(from, distance))
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("삽입할 수 없는 거리입니다."));

                final Section pastSection2 = sections.stream()
                        .filter(section -> section.existRight(to))
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("삽입할 수 없는 거리입니다."));

                System.out.println("changedSection2 = " + changedSection2);

                sections.remove(pastSection2);
                sections.add(changedSection2);
            }
            sections.add(new Section(line.getId(), from, to, distance));
            return;
        }
        throw new UnsupportedOperationException("처리할 수 없는 요청입니다.");
    }

    private boolean isLeftEnd(final Station to) {
        return sections.stream()
                .noneMatch(section -> section.existRight(to));
    }

    private boolean isRightEnd(final Station from) {
        return sections.stream()
                .noneMatch(section -> section.existLeft(from));
    }

    private boolean exist(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.exist(station));
    }

    public boolean isEmpty() {
        return sections.size() == 0;
    }

    public boolean hasStation(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.exist(station));
    }

    public void updateStation(final Station targetStation, final Station updateStation) {
        sections.stream()
                .filter(section -> section.exist(targetStation))
                .forEach(section -> section.updateStation(targetStation, updateStation));
    }

    public void deleteSection(final Line line, final Station targetStation) {
        final List<Section> targetSection = sections.stream()
                .filter(section -> section.exist(targetStation))
                .collect(Collectors.toUnmodifiableList());

        if (targetSection.size() == 1) {
            sections.remove(targetSection.get(0));
        }

        if (targetSection.size() == 2) {
            Station leftStation = null;
            Station rightStation = null;
            int distance = 0;

            for (Section section : targetSection) {
                if (section.existRight(targetStation)) {
                    leftStation = section.getFrom();
                    distance += section.getDistanceValue();
                }
                if (section.existLeft(targetStation)) {
                    rightStation = section.getTo();
                    distance += section.getDistanceValue();
                }
                sections.remove(section);
            }
            sections.add(new Section(line.getId(), leftStation, rightStation, distance));
        }
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean isHead(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.existLeft(station));
    }

    public List<Station> getOrderedStations(final Station station) {
        final List<Station> orderedStations = new ArrayList<>();
        Station targetStation = station;
        while (true) {

            for (Section section : sections) {
                if (section.existLeft(targetStation)) {
                    orderedStations.add(section.getFrom());
                    targetStation = section.getTo();
                    break;
                }
            }

            if (orderedStations.size() == sections.size()) {
                orderedStations.add(targetStation);
                break;
            }
        }
        return orderedStations;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}

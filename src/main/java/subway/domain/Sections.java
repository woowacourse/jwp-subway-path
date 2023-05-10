package subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Sections {

    private final List<Section> sections;

    public Sections() {
        this.sections = new ArrayList<>();
    }

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void insertInitially(final Station from, final Station to, final int distance) {
        sections.add(new Section(from, to, distance));
    }

    public void insert(final Station from, final Station to, final int distance) {
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
                        .orElseThrow(() -> new IllegalStateException("이거 무조건 됨"));


                final Section pastSection = sections.stream()
                        .filter(section -> section.existLeft(from))
                        .findAny()
                        .orElseThrow(() -> new IllegalStateException("이거 무조건 됨"));

                sections.remove(pastSection);
                sections.add(changedSection);
            }
            //추가 - 오른쪽 끝에 넣기
            sections.add(new Section(from, to, distance));
            return;
        }

        // 추가 - 사이 왼쪽에 넣기
        if (exist(to)) {
            if (!isLeftEnd(to)) {
                final Section changedSection2 = sections.stream()
                        .filter(section -> section.existRight(to) && section.isInsertable(distance))
                        .map(section -> section.changeRight(from, distance))
                        .findAny()
                        .orElseThrow(() -> new IllegalStateException("이거 무조건 됨"));

                final Section pastSection2 = sections.stream()
                        .filter(section -> section.existRight(to))
                        .findAny()
                        .orElseThrow(() -> new IllegalStateException("이거 무조건 됨"));

                System.out.println("changedSection2 = " + changedSection2);

                sections.remove(pastSection2);
                sections.add(changedSection2);
            }
            sections.add(new Section(from, to, distance));
            return;
        }
        throw new IllegalArgumentException("이건 진짜 나오면 안되는 거임");
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

    public List<Section> getSections() {
        return sections;
    }
}

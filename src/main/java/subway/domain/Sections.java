package subway.domain;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void add(final Section section) {
        if (sections.isEmpty()) { // 노선에 해당하는 구간이 아무것도 없는 경우
            section.initOrder();
            sections.add(section);
            return;
        }

        List<Section> upSections = findSectionsByStation(section.getUpStation());
        List<Section> downSections = findSectionsByStation(section.getDownStation());

        validateAlreadyExist(section);
        validateNotExistTargetStation(upSections, downSections);

        if (upSections.isEmpty()) { // 기준이 되는 역의 상행으로 추가
            addSectionInUpDirection(section, downSections);
        }

        if (downSections.isEmpty()) { // 기준이 되는 역의 하행으로 추가
            addSectionInDownDirection(section, upSections);
        }
    }

    private void validateAlreadyExist(final Section target) {
        sections.stream()
                .filter(section -> section.isSameSection(target))
                .findAny()
                .ifPresent(section -> {
                    throw new IllegalArgumentException("이미 존재하는 구간입니다.");
                });
    }

    private void validateNotExistTargetStation(final List<Section> upSections, final List<Section> downSections) {
        if (upSections.isEmpty() && downSections.isEmpty()) {
            throw new IllegalArgumentException("노선에 기준이 되는 역이 존재하지 않습니다.");
        }
    }

    private List<Section> findSectionsByStation(final Station section) {
        return sections.stream()
                .filter(it -> it.containStation(section))
                .collect(Collectors.toList());
    }

    private void addSectionInUpDirection(final Section section, final List<Section> downSections) {
        downSections.stream()
                .filter(downSection -> downSection.getUpStation().equals(section.getDownStation()) && downSection.getOrder() == 1)
                .findAny()
                .ifPresent((target) -> {
                    section.initOrder();
                    sections.add(section);
                    increaseOrder(section.getOrder(), section);
                });

        downSections.stream()
                .filter(downSection -> downSection.getDownStation().equals(section.getDownStation()))
                .findAny()
                .ifPresent((target) -> {
                    if (section.getDistance() >= target.getDistance()) {
                        throw new IllegalArgumentException("현재 구간보다 긴 구간을 추가할 수 없습니다.");
                    }
                    sections.remove(target);
                    sections.add(new Section(target.getUpStation(), section.getUpStation(), target.getDistance() - section.getDistance(), target.getOrder()));

                    section.updateOrder(target.getOrder() + 1);
                    sections.add(section);
                    increaseOrder(target.getOrder() + 1, section);
                });
    }

    private void addSectionInDownDirection(final Section section, final List<Section> upSections) {
        upSections.stream()
                .filter(upSection -> upSection.getDownStation().equals(section.getUpStation()) && upSection.getOrder() == sections.size())
                .findAny()
                .ifPresent((target) -> {
                    section.updateOrder(target.getOrder() + 1);
                    sections.add(section);
                    increaseOrder(target.getOrder() + 1, section);
                });

        upSections.stream()
                .filter(upSection -> upSection.getUpStation().equals(section.getUpStation()))
                .findAny()
                .ifPresent((target) -> {
                    if (section.getDistance() >= target.getDistance()) {
                        throw new IllegalArgumentException("현재 구간보다 긴 구간을 추가할 수 없습니다.");
                    }
                    sections.remove(target);
                    section.updateOrder(target.getOrder());
                    sections.add(section);

                    Section nextSection = new Section(section.getDownStation(), target.getDownStation(), target.getDistance() - section.getDistance(), target.getOrder() + 1);
                    sections.add(nextSection);
                    increaseOrder(target.getOrder() + 1, nextSection);
                });
    }

    public void remove(final Station station) {
        List<Section> deleteSections = sections.stream()
                .filter(section -> section.containStation(station))
                .collect(Collectors.toList());

        if (deleteSections.isEmpty()) {
            throw new NoSuchElementException("삭제할 구간이 존재하지 않습니다.");
        }

        if (deleteSections.size() == 2) {
            removeMiddleSection(deleteSections, station);
        }

        if (deleteSections.size() == 1) {
            removeFinalSection(deleteSections.get(0), station);
        }
    }

    private void removeFinalSection(final Section deleteSection, final Station deleteStation) {
        if (deleteSection.getUpStation().equals(deleteStation)) { // 상행 종점인 경우
            decreaseOrder(1, deleteSection);
        }

        sections.remove(deleteSection);
    }

    private void removeMiddleSection(final List<Section> deleteSections, final Station deleteStation) {
        Section previousSection = deleteSections.get(0);
        Section nextSection = deleteSections.get(1);
        int newDistance = previousSection.getDistance() + nextSection.getDistance();

        if (previousSection.getUpStation().equals(deleteStation)) {
            Station upStation = nextSection.getUpStation();
            Station downStation = previousSection.getDownStation();
            sections.add(new Section(upStation, downStation, newDistance, previousSection.getOrder()));
        }

        if (previousSection.getDownStation().equals(deleteStation)) {
            Station upStation = previousSection.getUpStation();
            Station downStation = nextSection.getDownStation();
            sections.add(new Section(upStation, downStation, newDistance, previousSection.getOrder()));
        }

        decreaseOrder(nextSection.getOrder(), nextSection);
        sections.removeAll(deleteSections);
    }

    private void increaseOrder(final int order, final Section target) {
        sections.stream()
                .filter(section -> section.getOrder() >= order && !section.equals(target))
                .forEach(Section::increaseOrder);
    }

    private void decreaseOrder(final int order, final Section target) {
        sections.stream()
                .filter(section -> section.getOrder() >= order && !section.equals(target))
                .forEach(Section::decreaseOrder);
    }

    public void clear() {
        sections.clear();
    }

    public int size() {
        return sections.size();
    }

    public List<Section> getSections() {
        return sections;
    }
}

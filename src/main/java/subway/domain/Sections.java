package subway.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public void add(final Section section) {
        if (sections.isEmpty()) { // 노선에 해당하는 구간이 아무것도 없는 경우
            sections.add(new Section(Station.empty(), section.getUpStation(), 0));
            sections.add(section);
            sections.add(new Section(section.getDownStation(), Station.empty(), 0));
            return;
        }

        List<Section> upSections = findSectionsByStation(section.getUpStation());
        List<Section> downSections = findSectionsByStation(section.getDownStation());

        if (upSections.isEmpty() && downSections.isEmpty()) {
            throw new IllegalArgumentException("노선에 기준이 되는 역이 존재하지 않습니다.");
        }

        if (upSections.isEmpty()) { // 기준이 되는 역의 상행으로 추가
            addSectionInUpDirection(section, downSections);
        }

        if (downSections.isEmpty()) { // 기준이 되는 역의 하행으로 추가
            addSectionInDownDirection(section, upSections);
        }
    }

    private List<Section> findSectionsByStation(final Station section) {
        return sections.stream()
                .filter(it -> it.containStation(section))
                .collect(Collectors.toList());
    }

    private void addSectionInUpDirection(final Section section, final List<Section> downSections) {
        downSections.stream()
                .filter(downSection -> downSection.getUpStation().isEmpty())
                .findAny()
                .ifPresent((target) -> {
                    sections.add(new Section(Station.empty(), section.getUpStation(), 0));
                    sections.remove(target);
                    sections.add(section);
                });

        downSections.stream()
                .filter(downSection -> downSection.getDownStation().equals(section.getDownStation()) && !downSection.getUpStation().isEmpty())
                .findAny()
                .ifPresent((target) -> {
                    if (section.getDistance() >= target.getDistance()) {
                        throw new IllegalArgumentException("현재 구간보다 긴 구간을 추가할 수 없습니다.");
                    }
                    sections.add(new Section(target.getUpStation(), section.getUpStation(), target.getDistance() - section.getDistance()));
                    sections.remove(target);
                    sections.add(section);
                });
    }

    private void addSectionInDownDirection(final Section section, final List<Section> upSections) {
        upSections.stream()
                .filter(upSection -> upSection.getDownStation().isEmpty())
                .findAny()
                .ifPresent((target) -> {
                    sections.add(new Section(section.getDownStation(), Station.empty(), 0));
                    sections.remove(target);
                    sections.add(section);
                });

        upSections.stream()
                .filter(upSection -> upSection.getUpStation().equals(section.getUpStation()) && !upSection.getDownStation().isEmpty())
                .findAny()
                .ifPresent((target) -> {
                    if (section.getDistance() >= target.getDistance()) {
                        throw new IllegalArgumentException("현재 구간보다 긴 구간을 추가할 수 없습니다.");
                    }
                    sections.add(new Section(section.getDownStation(), target.getDownStation(), target.getDistance() - section.getDistance()));
                    sections.remove(target);
                    sections.add(section);
                });
    }


    public void removeFinalSection(final Section deleteSection, final Station deleteStation) {
        Station upStation = deleteSection.getUpStation();
        Station downStation = deleteSection.getDownStation();

        if (downStation.equals(deleteStation)) { // 하행 종점인 경우
            sections.add(new Section(upStation, Station.empty(), 0));
        }

        if (upStation.equals(deleteStation)) { // 상행 종점인 경우
            sections.add(new Section(Station.empty(), downStation, 0));
        }

        sections.remove(deleteSection);
    }

    public void remove(final List<Section> deleteSections, final Station deleteStation) {
        Section previousSection = deleteSections.get(0);
        Section nextSection = deleteSections.get(1);
        int newDistance = previousSection.getDistance() + nextSection.getDistance();

        Station upStation = Station.empty();
        Station downStation = Station.empty();

        if (previousSection.getUpStation().equals(deleteStation)) {
            upStation = nextSection.getUpStation();
            downStation = previousSection.getDownStation();
        }

        if (previousSection.getDownStation().equals(deleteStation)) {
            upStation = previousSection.getUpStation();
            downStation = nextSection.getDownStation();
        }

        sections.add(new Section(upStation, downStation, newDistance));
        sections.removeAll(deleteSections);
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

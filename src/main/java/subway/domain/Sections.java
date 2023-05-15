package subway.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = sections;
    }

    private Section getUpFinalSection() {
        return sections.stream()
                .filter(Section::isUpFinalStation)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("상행 종점이 존재하지 않습니다."));
    }

    public void add(final Section section) {
        if (sections.size() == 0) { // 노선에 해당하는 구간이 아무것도 없는 경우
            sections.add(new Section(Station.empty(), section.getUpStation(), section.getDistance()));
            sections.add(section);
            sections.add(new Section(section.getDownStation(), Station.empty(), section.getDistance()));
            return;
        }

        List<Section> upSections = sections.stream()
                .filter(it -> it.containStation(section.getUpStation()))
                .collect(Collectors.toList());

        List<Section> downSections = sections.stream()
                .filter(it -> it.containStation(section.getDownStation()))
                .collect(Collectors.toList());

        if (upSections.isEmpty() && downSections.isEmpty()) {
            throw new IllegalArgumentException("노선에 기준이 되는 역이 존재하지 않습니다.");
        }

        if (upSections.isEmpty()) { // 기준이 되는 역의 상행으로 추가
            downSections.stream()
                    .filter(downSection -> downSection.getUpStation().isEmpty())
                    .findAny()
                    .ifPresent((target) -> {
                        sections.add(new Section(Station.empty(), section.getUpStation(), section.getDistance()));
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

        if (downSections.isEmpty()) { // 기준이 되는 역의 하행으로 추가
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
    }

    public void removeFinalSection(final Section deleteSection, final Station deleteStation) {

        if (deleteSection.getDownStation().equals(deleteStation)) { // 하행 종점인 경우
            Station upStation = deleteSection.getUpStation();
            sections.add(new Section(upStation, Station.empty(), 0));
            sections.remove(deleteSection);
            return;
        }
        // 상행 종점인 경우
        Station downStation = deleteSection.getDownStation();
        sections.add(new Section(Station.empty(), downStation, 0));
        sections.remove(deleteSection);
    }

    public void remove(final List<Section> deleteSections, final Station deleteStation) {
        Section previousSection = deleteSections.get(0);
        Section nextSection = deleteSections.get(1);
        int newDistance = previousSection.getDistance() + nextSection.getDistance();

        if (previousSection.getUpStation().equals(deleteStation)) {
            Station upStation = nextSection.getUpStation();
            Station downStation = previousSection.getDownStation();
            sections.add(new Section(upStation, downStation, newDistance));
            sections.removeAll(deleteSections);
            return;
        }

        Station upStation = previousSection.getUpStation();
        Station downStation = nextSection.getDownStation();
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

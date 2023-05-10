package subway.domain;

import java.util.List;
import java.util.Optional;

public class Sections {

    private List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        Station startStation = section.getStartStation();
        Station endStation = section.getEndStation();
        boolean hasStartStation = sections.stream()
                .anyMatch(section1 -> section1.hasStation(startStation));
        boolean hasEndStation = sections.stream()
                .anyMatch(section1 ->
                        section1.hasStation(endStation));
        if (hasStartStation && hasEndStation) {
            throw new IllegalArgumentException();
        }
        if (!hasStartStation && !hasEndStation) {
            throw new IllegalArgumentException();
        }
        if (hasStartStation) {
            Optional<Section> targetSectionOptional = sections.stream()
                    .filter(section1 -> section1.hasStartStation(startStation))
                    .findAny();

            if (targetSectionOptional.isPresent()) {
                Section targetSection = targetSectionOptional.get();
                Station tmpEndStation = targetSection.getEndStation();
                targetSection.updateEndStation(endStation);
                Section newSectionToAdd = new Section(endStation, tmpEndStation, 1);
                sections.add(newSectionToAdd);
                return;
            }
            sections.add(section);
        }
        if (hasEndStation) {
            Optional<Section> targetSectionOptional = sections.stream()
                    .filter(section1 -> section1.hasEndStation(endStation))
                    .findAny();

            if (targetSectionOptional.isPresent()) {
                Section targetSection = targetSectionOptional.get();
                Station tmpStartStation = targetSection.getStartStation();
                targetSection.updateStartStation(startStation);
                Section newSectionToAdd = new Section(tmpStartStation, startStation, 1);
                sections.add(newSectionToAdd);
                return;
            }
            sections.add(section);
        }
    }

    public List<Section> getSections() {
        return sections;
    }
}

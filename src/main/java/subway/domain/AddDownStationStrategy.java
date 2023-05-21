package subway.domain;

import java.util.List;
import java.util.Optional;

public class AddDownStationStrategy implements AddStationStrategy{

    @Override
    public void addNewStationToSection(List<Section> sections, Station baseStation, Station newStation, Distance newDistance) {
        Optional<Section> sectionToRevise = sections.stream()
                .filter(section -> section.isStationOnDirection(baseStation, Direction.DOWN))
                .findFirst();
        if (sectionToRevise.isPresent()) {
            Section origin = sectionToRevise.get();
            sections.remove(origin);
            sections.add(new Section(origin.getUpStation(), newStation, origin.getDistance().subtract(newDistance)));
            sections.add(new Section(newStation, baseStation, newDistance));
            return;
        }
        sections.add(new Section(newStation, baseStation, newDistance));
    }
}

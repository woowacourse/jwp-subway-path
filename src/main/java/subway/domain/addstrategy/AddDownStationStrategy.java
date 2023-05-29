package subway.domain.addstrategy;

import java.util.List;
import java.util.Optional;
import subway.domain.Direction;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

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

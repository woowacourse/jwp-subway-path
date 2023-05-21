package subway.domain.addstrategy;

import java.util.List;
import java.util.Optional;
import subway.domain.Direction;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

public class AddUpStationStrategy implements AddStationStrategy{

    @Override
    public void addNewStationToSection(List<Section> sections, Station baseStation, Station newStation, Distance newDistance) {
        Optional<Section> sectionToRevise = sections.stream()
                .filter(section -> section.isStationOnDirection(baseStation, Direction.UP))
                .findFirst();

        if (sectionToRevise.isPresent()) {
            Section origin = sectionToRevise.get();
            sections.remove(origin);
            sections.add(new Section(baseStation, newStation, newDistance));
            sections.add(new Section(newStation, origin.getDownStation(),
                    origin.getDistance().subtract(newDistance)));
            return;
        }
        sections.add(new Section(baseStation, newStation, newDistance));    }
}

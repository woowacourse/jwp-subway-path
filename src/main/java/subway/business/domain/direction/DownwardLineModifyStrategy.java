package subway.business.domain.direction;

import java.util.List;
import subway.business.domain.line.Section;
import subway.business.domain.line.Station;

public class DownwardLineModifyStrategy implements LineModifyStrategy {

    @Override
    public void addTerminus(Station station, List<Section> sections, int distance) {
        Section terminalSection = sections.get(sections.size() - 1);
        Station terminus = terminalSection.getDownwardStation();
        sections.add(sections.size(), Section.createToSave(terminus, station, distance));
    }

    @Override
    public void addMiddleStation(Station station, Station neighborhoodStation, List<Section> sections, int distance) {
        Section sectionToRemove = sections.stream()
                .filter(section -> section.getUpwardStation().hasNameOf(neighborhoodStation.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 이웃 역입니다. "
                        + "(입력한 이웃 역 : %s)", neighborhoodStation.getName())));
        Section sectionToAddUpward = Section.createToSave(
                sectionToRemove.getUpwardStation(),
                station,
                distance
        );
        Section sectionToAddDownward = Section.createToSave(
                station,
                sectionToRemove.getDownwardStation(),
                sectionToRemove.calculateRemainingDistance(distance)
        );
        int indexToAdd = sections.indexOf(sectionToRemove);
        sections.remove(sectionToRemove);
        sections.add(indexToAdd, sectionToAddDownward);
        sections.add(indexToAdd, sectionToAddUpward);
    }

    @Override
    public Station getTerminus(List<Section> sections) {
        return sections.get(sections.size() - 1).getDownwardStation();
    }
}


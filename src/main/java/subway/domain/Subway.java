package subway.domain;

import java.util.List;

public class Subway {
    private final Lines lines;

    public Subway(Lines lines) {
        this.lines = lines;
    }

    public static Subway from(List<Line> lines) {
        return new Subway(new Lines(lines));
    }

//    public AddSectionStrategy add(
//            long lineId,
//            long baseStationId,
//            Station newStation,
//            String directionValue,
//            int distance)
//    {
//        Line findline = lines.findLineBy(lineId);
//        Station baseStation = findline.getStation(baseStationId);
//
//        if(findline.isBlank()){
//            Section newSection = Section.from(baseStation, newStation, new Distance(distance));
//            return new FirstAddStrategy(newSection);
//        }
//
//        Direction direction = Direction.of(directionValue);
//        Section newSection = direction.createSection(baseStation, newStation, new Distance(distance));
//        return findline.add(direction, newSection, baseStation);
//    }

}

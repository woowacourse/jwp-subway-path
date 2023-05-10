package subway.domain.section;

import subway.domain.station.Station;

public class Section {

    private final Station source;
    private final Station target;
    private final SectionDistance distance;
    private final SectionType sectionType;

    public Section(final Station source, final Station target, final int distance, final SectionType sectionType) {
        this.source = source;
        this.target = target;
        this.distance = new SectionDistance(distance);
        this.sectionType = sectionType;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public int getDistance() {
        return distance.getDistance();
    }

    public SectionType getSectionType() {
        return sectionType;
    }
}

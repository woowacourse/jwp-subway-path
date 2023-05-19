package subway.domain.vo;

import subway.domain.SectionSorter;

import java.util.List;

public class RequestInclusiveSections {
    private final List<Section> sections;

    public RequestInclusiveSections(List<Section> sections) {

        this.sections = SectionSorter.sorting(sections);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean isSizeTwo() {
        return sections.size() == 2;
    }

    public int getDistance() {
        return sections.get(0).getDistanceValue() + sections.get(1).getDistanceValue();
    }

    public String getDeparture() {
        return sections.get(0).getDepartureValue();
    }

    public String getArrival() {
        return sections.get(1).getArrivalValue();
    }

    public long getDepartureId() {
        return sections.get(0).getId();
    }

    public long getArrivalId() {
        return sections.get(1).getId();
    }
}

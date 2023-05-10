package subway.domain;

public class Section {

    private Long id;
    private Stations stations;
    private Section to;

    public Section(final Stations stations) {
        this.stations = stations;
        to = null;
    }

    public Section find(final Section newSection) {
        Section current = this;

        while (current != null) {
            if (current.stations.isLinked(newSection.stations)) {
                return current;
            }
            current = current.to;
        }

        return null;
    }

    public void addNext(final Section newSection) {
        Section current = find(newSection);

        if (current.to == null) {
            current.to = newSection;
            return;
        }

        if (current.to.stations.isDistanceShorterThan(newSection.stations)) {
            throw new IllegalArgumentException("새로운 구간이 길이가 기존 구간의 길이보다 깁니다.");
        }

        addIntermediate(newSection, current);
    }

    private void addIntermediate(final Section newSection, final Section current) {
        newSection.to = current.to;
        current.to = newSection;

        newSection.to.stations.updateStationOnAdd(newSection.stations);
    }

    public void updateNextSection(final Section section) {
        to = section;
    }

    public boolean isLinked(final Section other) {
        return other.stations.isLinked(this.stations);
    }

    public Stations getStations() {
        return stations;
    }

    public Section getTo() {
        return to;
    }
}

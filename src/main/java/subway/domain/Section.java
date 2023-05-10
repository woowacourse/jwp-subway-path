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

    public boolean isLinked(final Section other) {
        return other.stations.isLinked(this.stations);
    }

    public void delete(final Station deletedStation) {
        final Section current = find(deletedStation);

        if (current == null) {
            throw new IllegalArgumentException("해당 Section에는 삭제할 역이 존재하지 않습니다.");
        }

        final Section deletedSection = current.to;

        current.stations.updateStationOnDelete(deletedSection.stations);

        updateSectionOnDelete(current, deletedSection);
    }

    private void updateSectionOnDelete(final Section current, final Section deletedSection) {
        current.to = deletedSection.to;
        deletedSection.to = null;
    }

    private Section find(final Station station) {
        Section current = this;

        while (current != null) {
            if (current.stations.getNext().isSame(station)) {
                return current;
            }
            current = current.to;
        }

        return null;
    }

    public void updateNextSection(final Section section) {
        to = section;
    }

    public Stations getStations() {
        return stations;
    }

    public Section getTo() {
        return to;
    }
}

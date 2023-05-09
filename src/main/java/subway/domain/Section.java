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

            if (current.stations.getNext().getName()
                                .equals(newSection.getStations().getCurrent().getName())) {
                return current;
            }

            current = current.to;
        }

        return null;
    }

    public void addNext(final Section newSection) {
        Section current = find(newSection);

        while (current != null && current.stations.getNext().getName()
                                                  .equals(newSection.getStations().getCurrent().getName())) {

            if (current.to == null) {
                //끝에 넣는거
                addLast(newSection);
                return;
            } else {
                newSection.to = current.to;
                current.to = newSection;

                System.out.println(current.getStations().getNext());
                System.out.println(newSection.to.getStations().getCurrent());

                final Section last = new Section(newSection.to.stations);

                newSection.to = last;
                last.getStations().getCurrent().setName(
                        newSection.getStations().getNext().getName()
                );
            }
            current = current.to;
        }
    }

    public void addLast(final Section newSection) {
        Section current = this;

        while (current.to != null) {
            current = current.to;
        }

        current.to = newSection;
    }

    public Stations getStations() {
        return stations;
    }

    public Section getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", stations=" + stations +
                ", to=" + to +
                '}';
    }
}

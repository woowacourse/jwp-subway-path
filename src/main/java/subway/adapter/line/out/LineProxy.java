package subway.adapter.line.out;

import java.util.ArrayList;
import java.util.List;
import subway.domain.interstation.InterStation;
import subway.domain.line.Line;

public class LineProxy extends Line {

    private final List<InterStation> removedInterStations = new ArrayList<>();
    private final List<InterStation> addedInterStations = new ArrayList<>();
    private boolean infoNeedToUpdated = false;

    public LineProxy(final Long id,
                     final String name,
                     final String color,
                     final List<InterStation> interStations) {
        super(id, name, color, interStations);
    }

    @Override
    public void deleteStation(final long existStationId) {
        final List<InterStation> beforeInterStations = new ArrayList<>(super.getInterStations().getInterStations());
        super.deleteStation(existStationId);
        calculateDifference(beforeInterStations);
    }

    @Override
    public void addInterStation(final Long existStationId, final Long newStationId, final long distance) {
        final List<InterStation> beforeInterStations = new ArrayList<>(super.getInterStations().getInterStations());
        super.addInterStation(existStationId, newStationId, distance);
        calculateDifference(beforeInterStations);
    }

    private void calculateDifference(final List<InterStation> beforeInterStations) {
        final List<InterStation> afterInterStations = new ArrayList<>(super.getInterStations().getInterStations());

        final List<InterStation> removed = new ArrayList<>(beforeInterStations);
        removed.removeAll(afterInterStations);
        removedInterStations.addAll(removed);

        final List<InterStation> added = new ArrayList<>(afterInterStations);
        added.removeAll(beforeInterStations);
        addedInterStations.addAll(added);
    }

    @Override
    public void updateName(final String name) {
        if (!super.getName().getValue().equals(name)) {
            infoNeedToUpdated = true;
        }
        super.updateName(name);
    }

    @Override
    public void updateColor(final String color) {
        if (!super.getColor().getValue().equals(color)) {
            infoNeedToUpdated = true;
        }
        super.updateColor(color);
    }

    public boolean isInfoNeedToUpdated() {
        return infoNeedToUpdated;
    }

    public List<InterStation> getRemovedInterStations() {
        return removedInterStations;
    }

    public List<InterStation> getAddedInterStations() {
        return addedInterStations;
    }
}

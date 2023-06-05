package subway.line.db;

import java.util.ArrayList;
import java.util.List;
import subway.interstation.domain.InterStation;
import subway.line.domain.Line;

public class LineProxy extends Line {

    private final List<InterStation> removedInterStations = new ArrayList<>();
    private final List<InterStation> addedInterStations = new ArrayList<>();
    private boolean infoNeedToUpdated = false;

    public LineProxy(Long id,
            String name,
            String color,
            List<InterStation> interStations) {
        super(id, name, color, interStations);
    }

    @Override
    public void deleteStation(long existStationId) {
        List<InterStation> beforeInterStations = new ArrayList<>(super.getInterStations().getInterStations());
        super.deleteStation(existStationId);
        calculateDifference(beforeInterStations);
    }

    @Override
    public void addInterStation(Long existStationId,
            Long downStationId,
            Long newStationId,
            long distance) {
        List<InterStation> beforeInterStations = new ArrayList<>(super.getInterStations().getInterStations());
        super.addInterStation(existStationId, downStationId, newStationId, distance);
        calculateDifference(beforeInterStations);
    }

    private void calculateDifference(List<InterStation> beforeInterStations) {
        List<InterStation> afterInterStations = new ArrayList<>(super.getInterStations().getInterStations());

        List<InterStation> removed = new ArrayList<>(beforeInterStations);
        removed.removeAll(afterInterStations);
        removedInterStations.addAll(removed);

        List<InterStation> added = new ArrayList<>(afterInterStations);
        added.removeAll(beforeInterStations);
        addedInterStations.addAll(added);
    }

    @Override
    public void updateName(String name) {
        if (!super.getName().getValue().equals(name)) {
            infoNeedToUpdated = true;
        }
        super.updateName(name);
    }

    @Override
    public void updateColor(String color) {
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

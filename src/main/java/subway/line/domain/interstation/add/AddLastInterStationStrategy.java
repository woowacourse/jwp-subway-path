package subway.line.domain.interstation.add;

import java.util.List;
import subway.line.domain.interstation.InterStation;

public class AddLastInterStationStrategy implements AddInterStationStrategy {

    @Override
    public boolean isSatisfied(List<InterStation> interStations, Long upStationId, Long downStationId,
            long newStationId) {
        if (downStationId != null) {
            return false;
        }
        if (isNotLastStation(interStations, upStationId)) {
            return false;
        }
        return hasNoCycle(interStations, newStationId);
    }

    private boolean isNotLastStation(List<InterStation> interStations, Long upStationId) {
        return !interStations.get(interStations.size() - 1).getDownStationId().equals(upStationId);
    }

    private boolean hasNoCycle(List<InterStation> interStations, long newStationId) {
        return interStations.stream().noneMatch(it -> it.contains(newStationId));
    }

    @Override
    public void addInterStation(List<InterStation> interStations, Long upStationId,
            Long downStationId,
            long newStationId, long distance) {
        InterStation interStation = new InterStation(upStationId, newStationId, distance);
        interStations.add(interStation);
    }
}

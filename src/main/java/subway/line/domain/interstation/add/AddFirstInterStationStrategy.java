package subway.line.domain.interstation.add;

import java.util.List;
import subway.line.domain.interstation.InterStation;

public class AddFirstInterStationStrategy implements AddInterStationStrategy {

    @Override
    public boolean isSatisfied(List<InterStation> interStations, Long upStationId, Long downStationId,
            long newStationId) {
        if (upStationId != null) {
            return false;
        }
        if (isNotFirstStation(interStations, downStationId)) {
            return false;
        }
        return hasNoCycle(interStations, newStationId);
    }

    private boolean isNotFirstStation(List<InterStation> interStations, Long downStationId) {
        return !interStations.get(0).getUpStationId().equals(downStationId);
    }

    private boolean hasNoCycle(List<InterStation> interStations, long newStationId) {
        return interStations.stream().noneMatch(it -> it.contains(newStationId));
    }

    @Override
    public void addInterStation(List<InterStation> interStations, Long upStationId,
            Long downStationId,
            long newStationId, long distance) {
        InterStation interStation = new InterStation(newStationId, downStationId, distance);
        interStations.add(0, interStation);
    }
}

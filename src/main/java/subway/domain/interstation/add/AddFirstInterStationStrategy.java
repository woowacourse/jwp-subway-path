package subway.domain.interstation.add;

import java.util.List;
import subway.domain.interstation.InterStation;

public class AddFirstInterStationStrategy implements AddInterStationStrategy {

    @Override
    public boolean isSatisfied(final List<InterStation> interStations, final Long upStationId, final Long downStationId,
            final long newStationId) {
        if (upStationId != null) {
            return false;
        }
        if (isNotFirstStation(interStations, downStationId)) {
            return false;
        }
        return hasNoCycle(interStations, newStationId);
    }

    private boolean isNotFirstStation(final List<InterStation> interStations, final Long downStationId) {
        return !interStations.get(0).getUpStationId().equals(downStationId);
    }

    private boolean hasNoCycle(final List<InterStation> interStations, final long newStationId) {
        return interStations.stream().noneMatch(it -> it.contains(newStationId));
    }

    @Override
    public void addInterStation(final List<InterStation> interStations, final Long upStationId,
            final Long downStationId,
            final long newStationId, final long distance) {
        final InterStation interStation = new InterStation(newStationId, downStationId, distance);
        interStations.add(0, interStation);
    }
}

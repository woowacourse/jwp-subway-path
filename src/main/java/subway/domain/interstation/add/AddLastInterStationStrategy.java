package subway.domain.interstation.add;

import java.util.List;
import subway.domain.interstation.InterStation;

public class AddLastInterStationStrategy implements AddInterStationStrategy {

    @Override
    public boolean isSatisfied(final List<InterStation> interStations, final Long upStationId, final Long downStationId,
            final long newStationId) {
        if (downStationId != null) {
            return false;
        }
        if (isNotLastStation(interStations, upStationId)) {
            return false;
        }
        return hasNoCycle(interStations, newStationId);
    }

    private boolean isNotLastStation(final List<InterStation> interStations, final Long upStationId) {
        return !interStations.get(interStations.size() - 1).getDownStationId().equals(upStationId);
    }

    private boolean hasNoCycle(final List<InterStation> interStations, final long newStationId) {
        return interStations.stream().noneMatch(it -> it.contains(newStationId));
    }

    @Override
    public void addInterStation(final List<InterStation> interStations, final Long upStationId,
            final Long downStationId,
            final long newStationId, final long distance) {
        final InterStation interStation = new InterStation(upStationId, newStationId, distance);
        interStations.add(interStation);
    }
}

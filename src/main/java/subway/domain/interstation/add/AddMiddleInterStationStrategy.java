package subway.domain.interstation.add;

import java.util.List;
import subway.domain.interstation.InterStation;
import subway.domain.interstation.exception.InterStationsException;

public class AddMiddleInterStationStrategy implements AddInterStationStrategy {

    @Override
    public boolean isSatisfied(final List<InterStation> interStations, final Long upStationId, final Long downStationId,
            final long newStationId) {
        if (upStationId == null || downStationId == null) {
            return false;
        }
        if (isNotMiddleStation(interStations, upStationId, downStationId)) {
            return false;
        }
        return hasNoCycle(interStations, newStationId);
    }

    private boolean isNotMiddleStation(final List<InterStation> interStations, final Long upStationId,
            final Long downStationId) {
        return interStations.stream().noneMatch(
                it -> it.getUpStationId().equals(upStationId) && it.getDownStationId().equals(downStationId));
    }

    private boolean hasNoCycle(final List<InterStation> interStations, final long newStationId) {
        return interStations.stream().noneMatch(it -> it.contains(newStationId));
    }


    @Override
    public void addInterStation(final List<InterStation> interStations, final Long upStationId,
            final Long downStationId,
            final long newStationId, final long distance) {
        final int index = findUpStationIndex(interStations, upStationId);
        final InterStation removedInterStation = interStations.remove(index);
        interStations.add(index, new InterStation(upStationId, newStationId, distance));
        interStations.add(index + 1,
                new InterStation(newStationId, downStationId, removedInterStation.getDistance().minus(distance)));
    }

    private int findUpStationIndex(final List<InterStation> interStations, final Long upStationId) {
        for (int i = 0; i < interStations.size(); i++) {
            if (interStations.get(i).getUpStationId().equals(upStationId)) {
                return i;
            }
        }
        throw new InterStationsException("구간이 연결되어있지 않습니다.");
    }
}

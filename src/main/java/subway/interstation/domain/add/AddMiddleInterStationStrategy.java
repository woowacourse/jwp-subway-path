package subway.interstation.domain.add;

import java.util.List;
import subway.interstation.domain.InterStation;
import subway.interstation.domain.exception.InterStationsException;

public class AddMiddleInterStationStrategy implements AddInterStationStrategy {

    @Override
    public boolean isSatisfied(List<InterStation> interStations, Long upStationId, Long downStationId,
            long newStationId) {
        if (upStationId == null || downStationId == null) {
            return false;
        }
        if (isNotMiddleStation(interStations, upStationId, downStationId)) {
            return false;
        }
        return hasNoCycle(interStations, newStationId);
    }

    private boolean isNotMiddleStation(List<InterStation> interStations, Long upStationId,
            Long downStationId) {
        return interStations.stream().noneMatch(
                it -> it.getUpStationId().equals(upStationId) && it.getDownStationId().equals(downStationId));
    }

    private boolean hasNoCycle(List<InterStation> interStations, long newStationId) {
        return interStations.stream().noneMatch(it -> it.contains(newStationId));
    }


    @Override
    public void addInterStation(List<InterStation> interStations, Long upStationId,
            Long downStationId,
            long newStationId, long distance) {
        int index = findUpStationIndex(interStations, upStationId);
        InterStation removedInterStation = interStations.remove(index);
        interStations.add(index, new InterStation(upStationId, newStationId, distance));
        interStations.add(index + 1,
                new InterStation(newStationId, downStationId, removedInterStation.getDistanceValue() - distance));
    }

    private int findUpStationIndex(List<InterStation> interStations, Long upStationId) {
        for (int i = 0; i < interStations.size(); i++) {
            if (interStations.get(i).getUpStationId().equals(upStationId)) {
                return i;
            }
        }
        throw new InterStationsException("구간이 연결되어있지 않습니다.");
    }
}

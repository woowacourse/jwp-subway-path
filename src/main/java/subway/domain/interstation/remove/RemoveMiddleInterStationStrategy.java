package subway.domain.interstation.remove;

import java.util.List;
import subway.domain.interstation.InterStation;
import subway.domain.interstation.exception.InterStationsException;

public class RemoveMiddleInterStationStrategy implements RemoveInterStationStrategy {

    @Override
    public boolean isSatisfied(final List<InterStation> interStations, final long removeStationId) {
        if (isFirst(interStations, removeStationId)) {
            return false;
        }
        if (isLast(interStations, removeStationId)) {
            return false;
        }
        return contains(interStations, removeStationId);
    }

    private boolean isFirst(final List<InterStation> interStations, final long removeStationId) {
        return interStations.get(0).getUpStationId() == removeStationId;
    }

    private boolean isLast(final List<InterStation> interStations, final long removeStationId) {
        return interStations.get(interStations.size() - 1).getDownStationId() == removeStationId;
    }

    private boolean contains(final List<InterStation> interStations, final long removeStationId) {
        return interStations.stream()
                .anyMatch(it -> it.getUpStationId() == removeStationId || it.getDownStationId() == removeStationId);
    }

    @Override
    public void removeInterStation(final List<InterStation> interStations, final long removeStationId) {
        final int index = findDownStationIndex(interStations, removeStationId);
        final InterStation downInterStation = interStations.remove(index + 1);
        final InterStation upInterStation = interStations.remove(index);
        interStations.add(index, new InterStation(upInterStation.getUpStationId(),
                downInterStation.getDownStationId(),
                upInterStation.getDistance().add(downInterStation.getDistance())));
    }

    private int findDownStationIndex(final List<InterStation> interStations, final long removeStationId) {
        for (int i = 0; i < interStations.size(); i++) {
            if (interStations.get(i).getDownStationId().equals(removeStationId)) {
                return i;
            }
        }
        throw new InterStationsException("역이 존재하지 않습니다.");
    }
}

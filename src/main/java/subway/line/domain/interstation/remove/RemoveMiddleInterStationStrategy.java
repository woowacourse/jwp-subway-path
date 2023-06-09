package subway.line.domain.interstation.remove;

import java.util.List;
import subway.line.domain.interstation.InterStation;
import subway.line.domain.interstation.exception.InterStationsException;

public class RemoveMiddleInterStationStrategy implements RemoveInterStationStrategy {

    @Override
    public boolean isSatisfied(List<InterStation> interStations, long removeStationId) {
        if (isFirst(interStations, removeStationId)) {
            return false;
        }
        if (isLast(interStations, removeStationId)) {
            return false;
        }
        return contains(interStations, removeStationId);
    }

    private boolean isFirst(List<InterStation> interStations, long removeStationId) {
        return interStations.get(0).getUpStationId() == removeStationId;
    }

    private boolean isLast(List<InterStation> interStations, long removeStationId) {
        return interStations.get(interStations.size() - 1).getDownStationId() == removeStationId;
    }

    private boolean contains(List<InterStation> interStations, long removeStationId) {
        return interStations.stream()
                .anyMatch(it -> it.getUpStationId() == removeStationId || it.getDownStationId() == removeStationId);
    }

    @Override
    public void removeInterStation(List<InterStation> interStations, long removeStationId) {
        int index = findDownStationIndex(interStations, removeStationId);
        InterStation downInterStation = interStations.remove(index + 1);
        InterStation upInterStation = interStations.remove(index);
        interStations.add(index, new InterStation(upInterStation.getUpStationId(),
                downInterStation.getDownStationId(),
                upInterStation.getDistanceValue() + downInterStation.getDistanceValue()));
    }

    private int findDownStationIndex(List<InterStation> interStations, long removeStationId) {
        for (int i = 0; i < interStations.size(); i++) {
            if (interStations.get(i).getDownStationId().equals(removeStationId)) {
                return i;
            }
        }
        throw new InterStationsException("역이 존재하지 않습니다.");
    }
}

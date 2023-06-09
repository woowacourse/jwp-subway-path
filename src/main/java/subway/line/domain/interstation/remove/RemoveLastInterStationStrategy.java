package subway.line.domain.interstation.remove;

import java.util.List;
import subway.line.domain.interstation.InterStation;

public class RemoveLastInterStationStrategy implements RemoveInterStationStrategy {

    @Override
    public boolean isSatisfied(List<InterStation> interStations, long removeStationId) {
        return interStations.get(interStations.size() - 1).getDownStationId().equals(removeStationId);
    }

    @Override
    public void removeInterStation(List<InterStation> interStations, long removeStationId) {
        interStations.remove(interStations.size() - 1);
    }
}

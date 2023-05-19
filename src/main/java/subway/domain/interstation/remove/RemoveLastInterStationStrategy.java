package subway.domain.interstation.remove;

import java.util.List;
import subway.domain.interstation.InterStation;

public class RemoveLastInterStationStrategy implements RemoveInterStationStrategy {

    @Override
    public boolean isSatisfied(final List<InterStation> interStations, final long removeStationId) {
        return interStations.get(interStations.size() - 1).getDownStationId().equals(removeStationId);
    }

    @Override
    public void removeInterStation(final List<InterStation> interStations, final long removeStationId) {
        interStations.remove(interStations.size() - 1);
    }
}

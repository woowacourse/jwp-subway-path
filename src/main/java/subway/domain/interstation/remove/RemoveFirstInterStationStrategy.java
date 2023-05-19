package subway.domain.interstation.remove;

import java.util.List;
import subway.domain.interstation.InterStation;

public class RemoveFirstInterStationStrategy implements RemoveInterStationStrategy {

    @Override
    public boolean isSatisfied(final List<InterStation> interStations, final long removeStationId) {
        return interStations.get(0).getUpStationId() == removeStationId;
    }

    @Override
    public void removeInterStation(final List<InterStation> interStations, final long removeStationId) {
        interStations.remove(0);
    }
}

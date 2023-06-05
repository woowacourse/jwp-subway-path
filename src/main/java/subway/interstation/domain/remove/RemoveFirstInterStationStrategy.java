package subway.interstation.domain.remove;

import java.util.List;
import subway.interstation.domain.InterStation;

public class RemoveFirstInterStationStrategy implements RemoveInterStationStrategy {

    @Override
    public boolean isSatisfied(List<InterStation> interStations, long removeStationId) {
        return interStations.get(0).getUpStationId() == removeStationId;
    }

    @Override
    public void removeInterStation(List<InterStation> interStations, long removeStationId) {
        interStations.remove(0);
    }
}

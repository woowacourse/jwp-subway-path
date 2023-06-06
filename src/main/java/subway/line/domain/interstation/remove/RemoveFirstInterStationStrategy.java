package subway.line.domain.interstation.remove;

import java.util.List;
import subway.line.domain.interstation.InterStation;

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

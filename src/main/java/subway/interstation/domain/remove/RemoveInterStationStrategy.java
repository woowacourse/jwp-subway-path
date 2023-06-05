package subway.interstation.domain.remove;

import java.util.List;
import subway.interstation.domain.InterStation;

public interface RemoveInterStationStrategy {

    boolean isSatisfied(List<InterStation> interStations, long removeStationId);

    void removeInterStation(List<InterStation> interStations, long removeStationId);
}

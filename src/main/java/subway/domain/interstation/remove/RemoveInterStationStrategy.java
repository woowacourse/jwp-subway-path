package subway.domain.interstation.remove;

import java.util.List;
import subway.domain.interstation.InterStation;

public interface RemoveInterStationStrategy {

    boolean isSatisfied(List<InterStation> interStations, final long removeStationId);

    void removeInterStation(List<InterStation> interStations, final long removeStationId);
}

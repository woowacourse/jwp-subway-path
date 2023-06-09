package subway.line.domain.interstation.remove;

import java.util.List;
import subway.line.domain.interstation.InterStation;

public interface RemoveInterStationStrategy {

    boolean isSatisfied(List<InterStation> interStations, long removeStationId);

    void removeInterStation(List<InterStation> interStations, long removeStationId);
}

package subway.domain.interstation.add;

import java.util.List;
import subway.domain.interstation.InterStation;

public interface AddInterStationStrategy {

    boolean isSatisfied(List<InterStation> interStations, Long upStationId, Long downStationId, long newStationId);

    void addInterStation(List<InterStation> interStations,
            Long upStationId,
            Long downStationId,
            long newStationId,
            long distance);
}

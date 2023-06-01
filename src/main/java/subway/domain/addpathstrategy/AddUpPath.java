package subway.domain.addpathstrategy;

import subway.domain.Path;
import subway.domain.Station;

import java.util.List;
import java.util.Map;

public class AddUpPath implements AddPathStrategy {
    @Override
    public Map<Station, Path> add(
            final Station targetStation,
            final Station addStation,
            final int distance,
            final List<Station> stations,
            final Map<Station, Path> paths
    ) {
        final int index = stations.indexOf(targetStation);
        if (paths.isEmpty()) {
            paths.put(addStation, new Path(targetStation, distance));
            return paths;
        }
        if (index == 0) {
            paths.put(addStation, new Path(targetStation, distance));
            return paths;
        }
        final Station stationBefore = stations.get(index - 1);
        final Path path = paths.get(stationBefore);
        validatePathDistance(distance, path);
        paths.put(stationBefore, new Path(addStation, path.getDistance() - distance));
        paths.put(addStation, new Path(targetStation, distance));
        return paths;
    }
}

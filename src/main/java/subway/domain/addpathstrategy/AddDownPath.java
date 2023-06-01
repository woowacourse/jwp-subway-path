package subway.domain.addpathstrategy;

import subway.domain.Path;
import subway.domain.Station;

import java.util.List;
import java.util.Map;

public class AddDownPath implements AddPathStrategy {
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
            paths.put(targetStation, new Path(addStation, distance));
            return paths;
        }
        if (index == stations.size() - 1) {
            paths.put(targetStation, new Path(addStation, distance));
            return paths;
        }
        final Path path = paths.get(targetStation);
        validatePathDistance(distance, path);
        paths.put(targetStation, new Path(addStation, distance));
        paths.put(addStation, new Path(path.getNext(), path.getDistance() - distance));
        return paths;
    }
}

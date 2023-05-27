package subway.domain.path;

public interface PathFinder {

    Path find(final String startStationName, final String endStationName);
}

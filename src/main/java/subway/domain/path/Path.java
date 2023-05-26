package subway.domain.path;

import subway.domain.section.Distance;
import subway.domain.section.Sections;
import subway.domain.station.Stations;

public class Path {

    private final Stations pathStations;
    private final Sections pathSections;

    public Path(final Stations pathStations, final Sections pathSections) {
        this.pathStations = pathStations;
        this.pathSections = pathSections;
    }

    public Stations getPathStations() {
        return pathStations;
    }

    public Distance getPathDistance() {
        return pathSections.totalDistance();
    }
}

package subway.domain.route;

import subway.domain.Station;
import subway.domain.vo.Distance;

import java.util.List;
import java.util.Objects;

public class Route {

    private final Station from;
    private final Station to;
    private final List<Station> transfers;
    private final List<RouteEdge> sections;
    private final Distance totalDistance;

    public Route(
            final Station from,
            final Station to,
            final List<Station> transfers,
            final List<RouteEdge> sections,
            final Distance totalDistance
    ) {
        this.from = from;
        this.to = to;
        this.transfers = transfers;
        this.sections = sections;
        this.totalDistance = totalDistance;
    }

    public Station getStart() {
        return from;
    }

    public Station getEnd() {
        return to;
    }

    public List<Station> getTransfers() {
        return transfers;
    }

    public List<RouteEdge> getRouteEdges() {
        return sections;
    }

    public Distance getTotalDistance() {
        return totalDistance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Route route = (Route) o;
        return Objects.equals(from, route.from)
                && Objects.equals(to, route.to)
                && Objects.equals(transfers, route.transfers)
                && Objects.equals(sections, route.sections)
                && Objects.equals(totalDistance, route.totalDistance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, transfers, sections, totalDistance);
    }
}

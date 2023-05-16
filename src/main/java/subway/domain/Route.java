package subway.domain;

import java.util.List;
import java.util.Objects;

public class Route {

    private Station from;
    private Station to;
    private List<Station> transfers;
    private List<Section> sections;
    private Distance totalDistance;
    private Money totalPrice;

    public Route(
            final Station from,
            final Station to,
            final List<Station> transfers,
            final List<Section> sections,
            final Distance totalDistance,
            final Money totalPrice
    ) {
        this.from = from;
        this.to = to;
        this.transfers = transfers;
        this.sections = sections;
        this.totalDistance = totalDistance;
        this.totalPrice = totalPrice;
    }

    public Station getFrom() {
        return from;
    }

    public Station getTo() {
        return to;
    }

    public List<Station> getTransfers() {
        return transfers;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Distance getTotalDistance() {
        return totalDistance;
    }

    public Money getTotalPrice() {
        return totalPrice;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Route route = (Route) o;
        return Objects.equals(from, route.from) && Objects.equals(to, route.to) && Objects.equals(transfers, route.transfers) && Objects.equals(sections, route.sections) && Objects.equals(totalDistance, route.totalDistance) && Objects.equals(totalPrice, route.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, transfers, sections, totalDistance, totalPrice);
    }
}

package subway.domain.fare;

import subway.domain.path.PathEdgeProxy;

import java.util.List;

public final class FareInfo {
    private final int fare;
    private final List<PathEdgeProxy> shortest;
    private final int age;

    public FareInfo(final int fare, final List<PathEdgeProxy> shortest, final int age) {
        this.fare = fare;
        this.shortest = shortest;
        this.age = age;
    }

    public boolean isOlderThan(final int age) {
        return this.age > age;
    }

    public int getDistance() {
        return shortest.stream()
                .mapToInt(PathEdgeProxy::getDistance)
                .sum();
    }

    public FareInfo addFare(final int fare) {
        return new FareInfo(this.fare + fare, shortest, age);
    }

    public FareInfo updateFare(final int fare) {
        return new FareInfo(fare, shortest, age);
    }

    public int getFare() {
        return fare;
    }

    public List<PathEdgeProxy> getShortest() {
        return shortest;
    }

    public int getAge() {
        return age;
    }
}

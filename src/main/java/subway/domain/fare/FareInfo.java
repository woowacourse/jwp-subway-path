package subway.domain.fare;

import subway.domain.PassengerType;
import subway.domain.route.Route;
import subway.domain.vo.Money;

public class FareInfo {

    private final LineDistance lineDistance;
    private final PassengerType passengerType;
    private final Money totalFare;

    private FareInfo(final LineDistance lineDistance, final PassengerType passengerType, final Money fare) {
        this.lineDistance = lineDistance;
        this.passengerType = passengerType;
        this.totalFare = fare;
    }

    public static FareInfo from(final PassengerType passengerType, final Route route) {
        return new FareInfo(LineDistance.from(route), passengerType, Money.ZERO);
    }

    public FareInfo plusFare(final Money plus) {
        return new FareInfo(lineDistance, passengerType, totalFare.plus(plus));
    }

    public FareInfo minusFare(final Money minus) {
        return new FareInfo(lineDistance, passengerType, totalFare.minus(minus));
    }

    public LineDistance getLineDistance() {
        return lineDistance;
    }

    public PassengerType getPassengerType() {
        return passengerType;
    }

    public Money getTotalFare() {
        return totalFare;
    }

    @Override
    public String toString() {
        return "FareInfo{" +
                "lineDistance=" + lineDistance +
                ", passengerType=" + passengerType +
                ", totalFare=" + totalFare +
                '}';
    }
}

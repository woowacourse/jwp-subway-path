package subway.domain.path;

import subway.domain.station.Station;

import java.util.List;

public class Path {

    private static final int BASIC_FEE = 1250;
    private static final int BASIC_FEE_STANDARD = 10;
    private static final int SURCHARGE = 100;
    private static final int SURCHARGE_STANDARD = 50;

    private final List<Station> stations;
    private final Distance distance;

    public Path(final List<Station> stations, final Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public Fee calculateFee() {
        if (distance.isLowerOrEqual(BASIC_FEE_STANDARD)) {
            return new Fee(BASIC_FEE);
        }
        if (distance.isLowerOrEqual(SURCHARGE_STANDARD)) {
            return new Fee(BASIC_FEE + calculateOver10Km(distance.calculateOverFare(BASIC_FEE_STANDARD)));
        }
        return new Fee(BASIC_FEE + calculateOver10Km(40) + calculateOver50Km(distance.calculateOverFare(SURCHARGE_STANDARD)));
    }

    private int calculateOver10Km(int overFare) {
        return (int) ((Math.ceil((overFare - 1) / 5) + 1) * SURCHARGE);
    }

    private int calculateOver50Km(int overFare) {
        return (int) ((Math.ceil((overFare - 1) / 8) + 1) * SURCHARGE);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance.getValue();
    }
}

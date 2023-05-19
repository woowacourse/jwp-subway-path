package subway.domain;

import java.util.List;

public class Path {

    public static final int BASIC_FEE = 1250;
    public static final int OVER_FEE = 100;
    private final List<String> pathStations;
    private final int distance;
    private final int fee;

    public Path(final List<String> pathStations, final int distance) {
        this.pathStations = pathStations;
        this.distance = distance;
        this.fee = calculateFee(distance);
    }

    private int calculateFee(final int distance) {
        if (distance <= 10) {
            return BASIC_FEE;
        }
        if (distance <= 50) {
            return BASIC_FEE + calculateOverFee(distance - 10);
        }
        return BASIC_FEE + calculateMostOverFee(distance - 50);
    }

    private int calculateOverFee(final int overDistance) {
        return (int) ((Math.ceil((overDistance - 1) / 5) + 1) * OVER_FEE);
    }

    private int calculateMostOverFee(final int overDistance) {
        return 800 + (int) ((Math.ceil((overDistance - 1) / 8) + 1) * OVER_FEE);
    }


    public List<String> getPathStations() {
        return pathStations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }
}

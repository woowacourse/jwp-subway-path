package subway.domain;

import java.util.List;

public class Path {

    public static final int BASIC_FEE = 1250;
    public static final int OVER_FEE = 100;
    public static final int BASE_DISTANCE = 10;
    public static final int OVER_DISTANCE = 50;
    public static final int OVER_UNIT = 5;
    public static final int MOST_OVER_UNIT = 8;
    public static final int OVER_DISTANCE_FEE = 800;
    private final List<String> pathStations;
    private final int distance;
    private final int fee;

    public Path(final List<String> pathStations, final int distance) {
        this.pathStations = pathStations;
        this.distance = distance;
        this.fee = calculateFee(distance);
    }

    private int calculateFee(final int distance) {
        if (distance <= BASE_DISTANCE) {
            return BASIC_FEE;
        }
        if (distance <= OVER_DISTANCE) {
            return BASIC_FEE + calculateOverFee(distance - BASE_DISTANCE);
        }
        return BASIC_FEE + calculateMostOverFee(distance - OVER_DISTANCE);
    }

    private int calculateOverFee(final int overDistance) {
        return (int) ((Math.ceil((overDistance - 1) / OVER_UNIT) + 1) * OVER_FEE);
    }

    private int calculateMostOverFee(final int overDistance) {
        return OVER_DISTANCE_FEE + (int) ((Math.ceil((overDistance - 1) / MOST_OVER_UNIT) + 1) * OVER_FEE);
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

package subway.domain.fee;

public class DistanceFee {

    private final static int NO_FEE = 0;

    private final int startBoundary;
    private final int endBoundary;
    private final int fee;
    private final int unit;

    public DistanceFee(int startBoundary, int endBoundary, int fee, int unit) {
        this.startBoundary = startBoundary;
        this.endBoundary = endBoundary;
        this.fee = fee;
        this.unit = unit;
    }

    public int calculateSectionFee(int distance) {
        if (startBoundary >= distance) {
            return NO_FEE;
        }
        if (distance <= endBoundary && (distance - startBoundary) % unit == 0) {
            return (distance - startBoundary) / unit * fee;
        }
        if (distance <= endBoundary && (distance - startBoundary) % unit != 0) {
            return ((distance - startBoundary) / unit + 1) * fee;
        }
        return (endBoundary - startBoundary) / unit * fee;
    }
}

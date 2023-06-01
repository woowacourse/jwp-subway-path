package subway.domain.fee;

import subway.domain.subway.Distance;

public class NormalFeeStrategy implements FeeStrategy{

    @Override
    public int calculateFee(Distance distance) {
        if (distance.isShorterSame(10)) {
            return 1250;
        }
        if (distance.isShorterSame(50)) {
            return 1250 + over10under50(distance.minusDistance(10).getDistance());
        }
        return 1250 + over10under50(40) + over50(distance.minusDistance(50).getDistance());
    }

    private int over10under50(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int over50(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

}

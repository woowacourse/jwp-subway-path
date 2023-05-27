package subway.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class DistanceChargeCalculator implements ChargeCalculator {
    @Value("${constant.charge.STANDARD}")
    private Long STANDARD;
    @Value("${constant.charge.PER_OVER}")
    private Long PER_OVER;
    @Value("${constant.distance.FIVE_KM}")
    private Long FIVE_KM;
    @Value("${constant.distance.EIGHT_KM}")
    private Long EIGHT_KM;
    @Value("${constant.distance.TEN_KM}")
    private Long TEN_KM;
    @Value("${constant.distance.FIFTY_KM}")
    private Long FIFTY_KM;

    @Override
    public Long calculateCharge(Long distance) {
        if (distance < TEN_KM) {
            return STANDARD;
        }

        final Long chargeForUnder50 = Math.min(distance, FIFTY_KM) - TEN_KM;
        long charge = (Math.floorDiv(chargeForUnder50, FIVE_KM) * PER_OVER) + STANDARD;

        if (distance > FIFTY_KM) {
            final Long chargeForOver50 = distance - FIFTY_KM;
            return charge + (Math.floorDiv(chargeForOver50, EIGHT_KM) * PER_OVER);
        }
        return charge;
    }
}

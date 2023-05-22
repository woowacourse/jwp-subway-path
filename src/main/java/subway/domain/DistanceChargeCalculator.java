package subway.domain;

import org.springframework.stereotype.Component;

@Component
public final class DistanceChargeCalculator implements ChargeCalculator {

    @Override
    public Long calculateCharge(Long distance) {
        if (distance < 10L) {
            return 1250L;
        }

        final Long chargeForUnder50 = Math.min(distance, 50) - 10;
        long charge = (Math.floorDiv(chargeForUnder50, 5) * 100) + 1250;

        if (distance > 50) {
            final Long chargeForOver50 = distance - 50;
            return charge + (Math.floorDiv(chargeForOver50, 8) * 100);
        }
        return charge;
    }
}

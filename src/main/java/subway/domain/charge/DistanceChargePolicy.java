package subway.domain.charge;

public interface DistanceChargePolicy {
    Charge apply(double totalDistance);
}

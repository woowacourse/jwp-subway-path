package subway.domain.fare.strategy.discount;

public enum AgeDiscountPolicy {
    TODDLER(0, 6, 0, 0),
    CHILD(6, 13, 350, 0.5),
    YOUTH(13, 18, 350, 0.8);

    private final int ageLowerStandard;
    private final int ageUpperStandard;
    private final int discountFare;
    private final double chargingRage;

    AgeDiscountPolicy(int ageLowerStandard, int ageUpperStandard, int discountFare, double chargingRage) {
        this.ageLowerStandard = ageLowerStandard;
        this.ageUpperStandard = ageUpperStandard;
        this.discountFare = discountFare;
        this.chargingRage = chargingRage;
    }

    public int getAgeLowerStandard() {
        return ageLowerStandard;
    }

    public int getAgeUpperStandard() {
        return ageUpperStandard;
    }

    public int getDiscountFare() {
        return discountFare;
    }

    public double getChargingRage() {
        return chargingRage;
    }
}

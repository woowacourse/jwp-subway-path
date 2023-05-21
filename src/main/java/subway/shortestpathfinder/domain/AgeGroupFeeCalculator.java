package subway.shortestpathfinder.domain;

public enum AgeGroupFeeCalculator {
    CHILD(350, 50), 
    TEENAGER(350, 20), 
    ADULT(0, 0);
    
    private final int deductionAmount;
    private final int discountPercent;
    
    AgeGroupFeeCalculator(final int deductAmount, final int discountPercent) {
        this.deductionAmount = deductAmount;
        this.discountPercent = discountPercent;
    }
    
    public Long calculateFee(final Long fee) {
        return (long) ((fee - deductionAmount) * ((100 - discountPercent) / 100.0));
    }
}

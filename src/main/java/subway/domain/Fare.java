package subway.domain;

public class Fare {
    private static final int MIN_FARE = 0;
    private final int value;

    public Fare(int value) {
        validate(value);
        this.value = value;
    }
    
    private void validate(int value) {
        if(value < MIN_FARE) {
            throw new IllegalArgumentException("요금은 양수여야 합니다");
        }
    }
    public int getValue() {
        return value;
    }
}

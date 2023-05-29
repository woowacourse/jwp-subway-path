package subway.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;

public enum PassengerAge {
    
    ADULT(age -> age >= 19, 0,0),
    TEENAGE(age -> 13 <= age && age <19, 350, 0.2),
    CHILD(age -> 6 <= age && age <13, 350,0.5),
    BABY(age -> age < 6, 0, 1);
    
    //연령에 따른 요금 할인 정책을 반영합니다.
    //청소년: 운임에서 350원을 공제한 금액의 20%할인
    //어린이: 운임에서 350원을 공제한 금액의 50%할인
    //- 청소년: 13세 이상~19세 미만
    //- 어린이: 6세 이상~13세 미만
    
    private final Predicate<Integer> range;
    private final int discountFare;
    private final double discountPercent;
    
    PassengerAge(Predicate<Integer> range, int discountFare, double discountPercent) {
        this.range = range;
        this.discountFare = discountFare;
        this.discountPercent = discountPercent;
    }
    
    public static PassengerAge from(int age) {
        return Arrays.stream(values())
                .filter(passengerAge -> passengerAge.range.test(age))
                .findFirst().get();
    }
    
    public int getDiscountFare() {
        return discountFare;
    }
    
    public double getDiscountPercent() {
        return discountPercent;
    }
}

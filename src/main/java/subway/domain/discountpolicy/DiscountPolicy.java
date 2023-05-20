package subway.domain.discountpolicy;

import subway.domain.Line;

import java.util.List;

public class DiscountPolicy implements FarePolicy {

    @Override
    public int calculateFare(final List<Line> boardingLines, final int distance, final int age, final int fare) {
        if (age >= 13 && age < 19) {
            return (int) ((fare - 350) * 0.8);
        }
        if (age >= 6 && age < 13) {
            return (int) ((fare - 350) * 0.5);
        }
        System.out.println(fare);
        return fare;
    }
}

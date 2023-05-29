package subway.domain.fare;

import org.springframework.stereotype.Component;

@Component
public class AgeDiscountStrategy implements DiscountStrategy{

    public int calculateDiscount(int fare, PassengerAge passengerAge) {
        int discountedFare = fare - passengerAge.getDiscountFare();
        discountedFare = discountedFare - (int) (discountedFare * passengerAge.getDiscountPercent());
        return discountedFare;
    }
    
}

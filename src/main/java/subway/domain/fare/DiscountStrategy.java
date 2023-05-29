package subway.domain.fare;

public interface DiscountStrategy {
    
    int calculateDiscount(int fare, PassengerAge passengerAge);
    
}

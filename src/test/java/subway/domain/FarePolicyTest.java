package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FarePolicyTest {
    
    @Test
    @DisplayName("요금을 계산한다 - 거리 10km, 일반 요금")
    void calculateFare() {
        final FarePolicy farePolicy = new FarePolicy(DiscountPolicy.NO_DISCOUNT);
        final int result = farePolicy.calculateFare(10);
        final int expected = 1250;
        Assertions.assertThat(result).isEqualTo(expected);
    }
    
    @Test
    @DisplayName("요금을 계산한다 - 거리 12km, 추가 요금")
    void calculateFare2() {
        final FarePolicy farePolicy = new FarePolicy(DiscountPolicy.NO_DISCOUNT);
        final int result = farePolicy.calculateFare(12);
        final int expected = 1350;
        Assertions.assertThat(result).isEqualTo(expected);
    }
    
    @Test
    @DisplayName("요금을 계산한다 - 거리 16km, 추가 요금")
    void calculateFare3() {
        final FarePolicy farePolicy = new FarePolicy(DiscountPolicy.NO_DISCOUNT);
        final int result = farePolicy.calculateFare(16);
        final int expected = 1450;
        Assertions.assertThat(result).isEqualTo(expected);
    }
    
    @Test
    @DisplayName("요금을 계산한다 - 거리 50km, 추가 요금")
    void calculateFare4() {
        final FarePolicy farePolicy = new FarePolicy(DiscountPolicy.NO_DISCOUNT);
        final int result = farePolicy.calculateFare(50);
        final int expected = 2050;
        Assertions.assertThat(result).isEqualTo(expected);
    }
    
    @Test
    @DisplayName("요금을 계산한다 - 거리 51km, 추가 요금")
    void calculateFare5() {
        final FarePolicy farePolicy = new FarePolicy(DiscountPolicy.NO_DISCOUNT);
        final int result = farePolicy.calculateFare(51);
        final int expected = 2150;
        Assertions.assertThat(result).isEqualTo(expected);
    }
    
}
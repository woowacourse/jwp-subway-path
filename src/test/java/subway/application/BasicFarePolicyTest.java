package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import subway.domain.general.Money;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WebMvcTest(FarePolicy.class)
class BasicFarePolicyTest {

    @Autowired
    private FarePolicy farePolicy;

    @BeforeEach
    void setUp() {
        farePolicy = new BasicDistanceProportionalPolicy();
    }

    @Test
    void lower_10_success() {
        Money money = farePolicy.getFareFrom(9);
        assertThat(money.getMoney()).isEqualTo(1250);
    }

    @Test
    void between_10_50_success() {
        Money money1 = farePolicy.getFareFrom(12);
        assertThat(money1.getMoney()).isEqualTo(1350);

        Money money2 = farePolicy.getFareFrom(16);
        assertThat(money2.getMoney()).isEqualTo(1450);

        Money money3 = farePolicy.getFareFrom(50);
        assertThat(money3.getMoney()).isEqualTo(2050);
    }

    @Test
    void over_50_success() {
        Money money = farePolicy.getFareFrom(58);
        assertThat(money.getMoney()).isEqualTo(2150);


        Money money2 = farePolicy.getFareFrom(64);
        assertThat(money2.getMoney()).isEqualTo(2250);
    }
}

package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PriceTest {

    public static final int DEFAULT_PRICE = 1_250;

    @ParameterizedTest
    @ValueSource(doubles = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void 이동_거리가_10km_이하이면_기본_운임이_부과된다(final double distance) {
        final Price price = Price.from(distance);

        assertThat(price.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @ParameterizedTest
    @CsvSource({
            "11, 1350",
            "14, 1350",
            "15, 1450",
            "19, 1450",
            "20, 1550",
            "24, 1550",
            "25, 1650",
            "29, 1650"
    })
    void 이동_거리가_10km_초과_50km_이하이면_5km당_100원씩_추가운임이_부과된다(int distance, int expectedPrice) {
        final Price price = Price.from(distance);
        assertThat(price.getPrice()).isEqualTo(expectedPrice);
    }

    @ParameterizedTest
    @CsvSource({
            "51, 2150",
            "59, 2250"
    })
    void 이동_거리가_50km_초과이면_8km당_100원씩_추가운임이_부과된다(int distance, int expectedPrice) {
        final Price price = Price.from(distance);
        assertThat(price.getPrice()).isEqualTo(expectedPrice);
    }
}

package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.route.Path;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("거리 비례 추가 요금 테스트")
class DistanceBasedExtraFarePolicyTest {
    @ParameterizedTest
    @CsvSource(value = {"9:0", "12:100", "16:200", "58:900"}, delimiter = ':')
    void 거리에_따른_추가_요금을_계산한다(final int 거리, final int 예상_추가_요금) {
        // given
        Path 경로 = new Path(Collections.emptyList(), 거리);
        DistanceBasedExtraFarePolicy 거리_비례_추가_요금_정책 = new DistanceBasedExtraFarePolicy();

        // when
        Fare 추가_요금 = 거리_비례_추가_요금_정책.calculateExtraFare(경로);

        // then
        assertThat(추가_요금).isEqualTo(new Fare(예상_추가_요금));
    }
}

package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.core.Distance;
import subway.domain.path.PathFindResult;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BaseFarePolicyTest {

    @Test
    void 기본운임은_입력받은_운임에_1250원을_더한값을_반환한다() {
        // given
        final FarePolicy baseFarePolicy = new BaseFarePolicy();
        final PathFindResult pathFindResult = new PathFindResult(new Distance(9), Collections.emptyList());
        final Passenger passenger = new Passenger();

        // when
        final int result = baseFarePolicy.calculate(pathFindResult, passenger, 0);

        // then
        assertThat(result).isEqualTo(1250);
    }
}

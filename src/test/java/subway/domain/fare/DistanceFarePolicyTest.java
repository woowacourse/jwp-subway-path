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
class DistanceFarePolicyTest {

    private FarePolicy farePolicy = new DistanceFarePolicy();

    @Test
    void 입력받은_거리가_10키로_이하인_경우_추가운임이_발생하지_않는다() {
        // given
        final PathFindResult pathFindResult = new PathFindResult(new Distance(9), Collections.emptyList());
        final Passenger passenger = new Passenger();

        // when
        final int result = farePolicy.calculate(pathFindResult, passenger, 1250);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(1250);
    }

    @Test
    void 입력받은_거리가_10키로를_초과하고_50키로_이하인_경우_추가운임이_5KM당_100원이_발생한다() {
        // given
        final PathFindResult pathFindResult = new PathFindResult(new Distance(27), Collections.emptyList());
        final Passenger passenger = new Passenger();

        // when
        final int result = farePolicy.calculate(pathFindResult, passenger, 1250);

        // then
        assertThat(result).isEqualTo(1650);
    }

    @Test
    void 입력받은_거리가_50키로를_초과하는_경우_추가운임이_8KM당_100원이_발생한다() {
        // given
        final PathFindResult pathFindResult = new PathFindResult(new Distance(61), Collections.emptyList());
        final Passenger passenger = new Passenger();

        // when
        final int result = farePolicy.calculate(pathFindResult, passenger, 1250);

        // then
        assertThat(result).isEqualTo(2250);
    }
}
